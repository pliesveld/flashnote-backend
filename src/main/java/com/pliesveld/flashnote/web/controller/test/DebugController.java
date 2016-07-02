package com.pliesveld.flashnote.web.controller.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.customProperties.ValidationSchemaFactoryWrapper;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.web.validator.StringEnumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/debug")
public class DebugController {
    private static final Logger LOG = LogManager.getLogger();

    private static final Instant STARTUP_TIMESTAMP = Instant.now();

    ObjectMapper objectMapper;

    @Autowired
    public DebugController(HibernateAwareObjectMapperImpl hibernateAwareObjectMapper) {
        this.objectMapper = hibernateAwareObjectMapper.copy();
    }

    protected static void updateLog(DebugRequestJson.LOG_TYPE LOG_TAG, DebugRequestJson.LOG_LEVEL LOG_LEVEL)
    {
        LOG.debug(Markers.DEBUG, "Setting {} to {}", LOG_TAG, LOG_LEVEL);
        System.setProperty(LOG_TAG.toString(), LOG_LEVEL.toString());
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    protected static void updateLog(String log_tag, String log_level)
    {
        LOG.debug(Markers.DEBUG, "Setting {} to {} ; no update", log_tag, log_level);
        System.setProperty(log_tag, log_level);
    }

    protected static void updateLogContext()
    {
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    @RequestMapping(value = {"/startup"}, method = RequestMethod.GET)
    public ResponseEntity<?> startupTimestamp()
    {
        return ResponseEntity.ok(STARTUP_TIMESTAMP.getEpochSecond());
    }

    @SuppressWarnings("unchecked")
    protected Map<String,Object> writeAndMap(ObjectMapper m, Object value)
            throws IOException
    {
        String str = m.writeValueAsString(value);
        return (Map<String,Object>) m.readValue(str, Map.class);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getLogLevels(@RequestParam("class") String clazz_name, @RequestParam(name = "validate", defaultValue = "false") boolean validate) throws IOException {
        Class<?>  view_clazz = null;

        try {
//            LOG.debug(Markers.DEBUG, "Checking class: {}", clazz_name);

            Class<?> clazz = Class.forName(clazz_name);

            JsonSchema jsonSchema;

            if ( validate ) {
                ValidationSchemaFactoryWrapper visitor = new ValidationSchemaFactoryWrapper();
                objectMapper.acceptJsonFormatVisitor(clazz, visitor);
                jsonSchema = visitor.finalSchema();
            } else {
                JsonSchemaGenerator generator = new JsonSchemaGenerator(objectMapper);
                jsonSchema = generator.generateSchema(clazz);
            }

            Map<String, Object> result = writeAndMap(objectMapper, jsonSchema);
            return ResponseEntity.ok(result);
        } catch (ClassNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(value = {"/log", "/logs"}, method = RequestMethod.GET)
    public ResponseEntity<?> getLogLevels()
    {
        Map<String,String> settings = Stream.of(DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
                ((s) -> {
                    return s;
                }), (m) -> {
            return System.getProperty(m, "");
        })
        );

        return ResponseEntity.ok(settings);
    }

    @RequestMapping(value = "/log", method = RequestMethod.PUT)
    public ResponseEntity<?> changeSingleLogLevelWithRequestParamAsString(
            @RequestParam("log") @Valid @StringEnumeration(enumClass = DebugRequestJson.LOG_TYPE.class) String log,
            @RequestParam("level") @Valid @StringEnumeration(enumClass = DebugRequestJson.LOG_LEVEL.class) String level)
    {
        DebugRequestJson.LOG_TYPE debugLog = DebugRequestJson.LOG_TYPE.valueOf(log);
        DebugRequestJson.LOG_LEVEL debugLevel = DebugRequestJson.LOG_LEVEL.valueOf(level);

        updateLog(debugLog, debugLevel);

        Map<String,String> settings = Stream.of(DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
            ((s) -> {
                return s;
            }), (m) -> {
                return System.getProperty(m, "");
            })
        );

        return ResponseEntity.ok(settings);
    }

//
//    @RequestMapping(value = "/log2", method = RequestMethod.PUT)
//    public ResponseEntity<?> changeSingleLogLevelWithRequestParamAsEnum(@RequestParam("log") DebugRequestJson.LOG debugLog, @RequestParam("level") DebugRequestJson.LEVEL debugLevel)
//    {
//        updateLog(debugLog, debugLevel);
//
//        Map<String,String> settings = Stream.of (DebugRequestJson.LOG.values()).map(l -> l.toString()).collect(Collectors.toMap(
//            ((s) -> {
//                return s;
//            }), (m) -> {
//                return System.getProperty(m, "");
//            })
//        );
//
//        return ResponseEntity.ok(settings);
//    }
//

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public ResponseEntity<?> updateLogLevels(@Valid @RequestBody DebugRequestJson debugLog)
    {
        updateLog(debugLog.getLog(),debugLog.getLevel());

        Map<String,String> settings = Stream.of(DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
                ((s) -> {
                    return s;
                }), (m) -> {
            return System.getProperty(m, "");
        })
        );

        return ResponseEntity.ok(settings);
    }

    @RequestMapping(value = "/logs", method = RequestMethod.POST)
    public ResponseEntity<?> updateLogGroupLevels(@Valid @RequestBody GroupDebugRequestJson debugLogGroup)
    {

        Map<String,String> prior = getCurrentSettings();

        int prior_hc = prior.hashCode();
        for ( DebugRequestJson debugRequestJson : debugLogGroup.getSettings() )
        {
//            LOG.error("checking {} {}", debugRequestJson.getLog(), debugRequestJson.getLevel());
            updateLog(debugRequestJson.getLog(),debugRequestJson.getLevel());
        }

        Map<String,String> settings = getCurrentSettings();

        updateLogContext();

        return ResponseEntity.ok(settings);
    }

    private Map<String,String> getCurrentSettings() {
        return Stream.of(DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
                ((s) -> {
                    return s;
                }), (m) -> {
            return System.getProperty(m, "");
        })
        );
    }

    public static class DebugRequestJson {
        public enum LOG_TYPE {
            LOG_SQL_LEVEL,
            LOG_ENTITY_LEVEL,
            LOG_TRANS_LEVEL,
            LOG_TRANS_SYNC_LEVEL,
            LOG_MVC_LEVEL,
            LOG_SECURITY_LEVEL,
            LOG_APP_LEVEL,
            LOG_SPRING_LEVEL,
            LOG_BEANS_LEVEL,
            LOG_BEANS_CACHED_LEVEL
        }

        public enum LOG_LEVEL {
            TRACE,
            DEBUG,
            INFO,
            WARN,
            ERROR
        }


        @NotNull
        @JsonProperty
        LOG_TYPE log;

        @NotNull
        @JsonProperty
        LOG_LEVEL level;

        public DebugRequestJson() {
        }

        public DebugRequestJson(LOG_TYPE log, LOG_LEVEL level) {
            this.log = log;
            this.level = level;
        }

        public LOG_TYPE getLog() {
            return log;
        }

        public void setLog(LOG_TYPE log) {
            this.log = log;
        }


        public LOG_LEVEL getLevel() {
            return level;
        }

        public void setLevel(LOG_LEVEL level) {
            this.level = level;
        }
    }


    @Validated
    public static class GroupDebugRequestJson {

        @NotNull
        @JsonProperty
        @Size(min = 1)
        ArrayList<DebugRequestJson> settings = new ArrayList<>();

        public GroupDebugRequestJson() {
        }

        public GroupDebugRequestJson(ArrayList<DebugRequestJson> settings) {
            this.settings = settings;
        }

        public ArrayList<DebugRequestJson> getSettings() {
            return settings;
        }

        public void setSettings(@Valid ArrayList<DebugRequestJson> settings) {
            this.settings = settings;
        }
    }

    @PostConstruct
    public void postConstruct() {

        Properties props = System.getProperties();

//        props.forEach((k, v) -> {
//            LOG.debug(Markers.DEBUG,"key {} = {}", k, v);
//        });

        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(true)).reconfigure();
        for (DebugRequestJson.LOG_TYPE log : DebugRequestJson.LOG_TYPE.values()) {
            //DebugController.updateLog(log, DebugRequestJson.LEVEL.WARN);
            final String level = System.getProperty(log.toString());
            if (level != null)
                LOG.debug(Markers.DEBUG, "{} => {}", log, level);
        }

//        props.forEach((k,v) -> {
//            LOG.debug(Markers.DEBUG,"key {} = {}", k, v);
//        });

    }
}
