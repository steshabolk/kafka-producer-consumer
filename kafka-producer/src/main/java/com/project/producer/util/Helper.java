package com.project.producer.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class Helper {

    public static Integer getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    public static void info(String s) {
        log.info("| {} |", s);
    }

    public static void error(String s) {
        log.error("| {} |", s);
    }
}
