package com.pliesveld.flashnote.web.controller.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.web.validator.StringEnumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value="/admin")
public class DebugController {
    private static final Logger LOG = LogManager.getLogger();

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

    @RequestMapping(value = {"/log", "/logs"}, method = RequestMethod.GET)
    public ResponseEntity<?> getLogLevels()
    {
        Map<String,String> settings = Stream.of (DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
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

        Map<String,String> settings = Stream.of (DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
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

        Map<String,String> settings = Stream.of (DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
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
        for( DebugRequestJson debugRequestJson : debugLogGroup.getSettings() )
        {
//            LOG.error("checking {} {}", debugRequestJson.getLog(), debugRequestJson.getLevel());
            updateLog(debugRequestJson.getLog(),debugRequestJson.getLevel());
        }

        Map<String,String> settings = getCurrentSettings();

        updateLogContext();

        return ResponseEntity.ok(settings);
    }

    private Map<String,String> getCurrentSettings() {
        return Stream.of (DebugRequestJson.LOG_TYPE.values()).map(l -> l.toString()).collect(Collectors.toMap(
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
        LOG.debug(Markers.DEBUG, "Calling POST CONSTRUCT");

        Properties props = System.getProperties();

//        props.forEach((k, v) -> {
//            LOG.debug(Markers.DEBUG,"key {} = {}", k, v);
//        });

        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(true)).reconfigure();
        for(DebugRequestJson.LOG_TYPE log : DebugRequestJson.LOG_TYPE.values()) {
            //DebugController.updateLog(log, DebugRequestJson.LEVEL.WARN);
            LOG.debug(Markers.DEBUG, "{} => {}", log, System.getProperty(log.toString()));

        }


//        props.forEach((k,v) -> {
//            LOG.debug(Markers.DEBUG,"key {} = {}", k, v);
//        });

    }
}
