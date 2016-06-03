package com.pliesveld.tests.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Allows unit tests to include SQL statements in their output.
 *
 * A TextExecutionListener sets the system property LOG_SQL_LEVEL and refreshes the log4j2 configuration file
 * before and after each test.  log4j2 has been configured to set the log level of the the logger
 * that on org.hibernate.SQL.
 *
 * Sample log4j2-test.xml file:
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <Configuration configDebug="false" shutdownHook="disable">
 *   <Properties>
 *     <Property name="LOG_SQL_LEVEL">WARN</Property>
 *
 *   </Properties>
 *
 *   <Appenders>
 *     <Console name="CONSOLE" target="SYSTEM_OUT">
 *       <PatternLayout pattern="%msg%n"/>
 *     </Console>
 *   </Appenders>
 *
 *   <Loggers>
 *     <logger name="org.hibernate.SQL" level="${sys:LOG_SQL_LEVEL}" additivity="false">
 *       <AppenderRef ref="CONSOLE"/>
 *     </logger>
 *   </Loggers>
 *
 * </Configuration>
 */

public class LogHibernateTestExecutionListener extends AbstractTestExecutionListener {
    private static final Logger LOG = LogManager.getLogger("org.hibernate.SQL");
    private static final String LOG_SQL_TAG = "LOG_SQL_LEVEL";

    protected static void disableSQL()
    {
        System.setProperty(LOG_SQL_TAG, "WARN");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    protected static void enableSQL()
    {
        System.setProperty(LOG_SQL_TAG, "DEBUG");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    public void beforeTestClass(TestContext testContext) throws Exception {
        disableSQL();
    }

    public void beforeTestMethod(TestContext testContext) throws Exception {
        enableSQL();
        LOG.debug("Executing {}#{}",testContext.getTestClass().getName(),testContext.getTestMethod().getName());
    }

    public void afterTestMethod(TestContext testContext) throws Exception {

        Throwable thrown = testContext.getTestException();

        /**
         * if an exception is thrown in the test method, unwind the exception hierarchy, and find the root
         * exception.  During the unwinding clear the stack trace elements.
         */
//        if(thrown != null)
//        {
//            Throwable cause = thrown;
//            Throwable last = null;
//
//
//            while(cause != null)
//            {
//                last = cause;
//
//                if(cause.getClass() != AssertionError.class)
//                    cause.setStackTrace(new StackTraceElement[]{});
//
//                cause = cause.getCause();
//                if(cause != null)
//                    last = cause;
//            }
//
////            LOG.debug(last.toString());
//            if(thrown.getClass() != AssertionError.class)
//                thrown.setStackTrace(new StackTraceElement[]{});
//
//            testContext.updateState(
//                testContext.getTestInstance(),
//                testContext.getTestMethod(),
//                last
//            );
//
//        }
        disableSQL();
    }

}
