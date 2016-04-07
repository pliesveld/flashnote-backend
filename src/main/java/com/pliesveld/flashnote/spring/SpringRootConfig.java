package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.audit.DateTimeService;
import com.pliesveld.flashnote.audit.DateTimeServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository",
        "com.pliesveld.flashnote.security"
})
@EnableScheduling
public class SpringRootConfig implements AsyncConfigurer, SchedulingConfigurer {
    private static final Logger LOG = LogManager.getLogger();
    private static final Logger schedulingLogger =
            LogManager.getLogger(LOG.getName() + ".[scheduling]");
    private static final Logger schedulingLoggerError =
            LogManager.getLogger(LOG.getName() + ".[scheduling-error]");




    //    @Profile({Profiles.LOCAL})
    @Bean
    DateTimeService currentTimeDateTimeService()
    {
        return new DateTimeServiceImpl();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler()
    {
        LOG.info("Setting up thread pool task scheduler with 20 threads.");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(t -> schedulingLogger.error(
                "Unknown error occurred while executing task.", t
        ));
        scheduler.setRejectedExecutionHandler(
                (r, e) -> schedulingLogger.error(
                        "Execution of task {} was rejected for unknown reasons.", r
                )
        );
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar)
    {
        TaskScheduler scheduler = this.taskScheduler();
        LOG.info("Configuring scheduled method executor {}.", scheduler);
        registrar.setTaskScheduler(scheduler);
    }

    @Override
    public Executor getAsyncExecutor() {
        Executor executor = this.taskScheduler();
        LOG.info("Configuring asynchronous method executor {}.", executor);
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> { schedulingLoggerError.warn(method.getName(), ex); };
    }


}
