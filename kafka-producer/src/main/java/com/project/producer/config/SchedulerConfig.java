package com.project.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.project.producer.util.Helper.error;

@Configuration
@EnableAsync
@EnableScheduling
@ConditionalOnProperty(value = "scheduler.enable", havingValue = "true", matchIfMissing = true)
public class SchedulerConfig {

    @Value("${scheduler.pool-size}")
    private Integer poolSize;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setErrorHandler(t -> error("scheduler error: " + t.getMessage()));
        return threadPoolTaskScheduler;
    }
}
