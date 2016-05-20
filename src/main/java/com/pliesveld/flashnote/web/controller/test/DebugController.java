package com.pliesveld.flashnote.web.controller.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.web.validator.StringEnumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value="/admin")
public class DebugController {
    private static final Logger LOGGER = LogManager.getLogger();

    protected static void updateLog(DebugRequestJson.LOG LOG_TAG, DebugRequestJson.LEVEL LOG_LEVEL)
    {
        LOGGER.debug("Setting {} to {}", LOG_TAG, LOG_LEVEL);
        System.setProperty(LOG_TAG.toString(), LOG_LEVEL.toString());
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    protected static void updateLog(String log_tag, String log_level)
    {
        LOGGER.debug("Setting {} to {} ; no update", log_tag, log_level);
        System.setProperty(log_tag, log_level);
    }

    protected static void updateLogContext()
    {
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public ResponseEntity<?> getLogLevels()
    {
        Map<String,String> settings = Stream.of (DebugRequestJson.LOG.values()).map(l -> l.toString()).collect(Collectors.toMap(
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
            @RequestParam("log") @Valid @StringEnumeration(enumClass = DebugRequestJson.LOG.class) String log,
            @RequestParam("level") @Valid @StringEnumeration(enumClass = DebugRequestJson.LEVEL.class) String level)
    {
        DebugRequestJson.LOG debugLog = DebugRequestJson.LOG.valueOf(log);
        DebugRequestJson.LEVEL debugLevel = DebugRequestJson.LEVEL.valueOf(level);

        updateLog(debugLog, debugLevel);

        Map<String,String> settings = Stream.of (DebugRequestJson.LOG.values()).map(l -> l.toString()).collect(Collectors.toMap(
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

        Map<String,String> settings = Stream.of (DebugRequestJson.LOG.values()).map(l -> l.toString()).collect(Collectors.toMap(
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
//            LOGGER.error("checking {} {}", debugRequestJson.getLog(), debugRequestJson.getLevel());
            updateLog(debugRequestJson.getLog(),debugRequestJson.getLevel());
        }

        Map<String,String> settings = getCurrentSettings();

        updateLogContext();

        return ResponseEntity.ok(settings);
    }

    private Map<String,String> getCurrentSettings() {
        return Stream.of (DebugRequestJson.LOG.values()).map(l -> l.toString()).collect(Collectors.toMap(
                ((s) -> {
                    return s;
                }), (m) -> {
            return System.getProperty(m, "");
        })
        );
    }


    public static class DebugRequestJson {
        public enum LOG {
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

        public enum LEVEL {
            TRACE,
            DEBUG,
            INFO,
            WARN,
            ERROR
        }


        @NotNull
        @JsonProperty
        LOG log;

        @NotNull
        @JsonProperty
        LEVEL level;

        public DebugRequestJson() {
        }

        public DebugRequestJson(LOG log, LEVEL level) {
            this.log = log;
            this.level = level;
        }

        public LOG getLog() {
            return log;
        }

        public void setLog(LOG log) {
            this.log = log;
        }


        public LEVEL getLevel() {
            return level;
        }

        public void setLevel(LEVEL level) {
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

//    @PostConstruct
//    public void postConstruct() {
//        LOGGER.debug("Calling POST CONSTRUCT");
//
//        Properties props = System.getProperties();
//
////        props.forEach((k, v) -> {
////            LOGGER.debug("key {} = {}", k, v);
////        });
//
//        for(DebugRequestJson.LOG log : DebugRequestJson.LOG.values()) {
//            DebugController.updateLog(log, DebugRequestJson.LEVEL.WARN);
//        }
//        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
//
////        props.forEach((k,v) -> {
////            LOGGER.debug("key {} = {}", k, v);
////        });
//
//    }
}
