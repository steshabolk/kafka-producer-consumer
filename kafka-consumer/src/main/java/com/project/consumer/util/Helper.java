package com.project.consumer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    static Logger log = LoggerFactory.getLogger(Helper.class);

    public static void info(String s) {
        log.info("| {} |", s);
    }
}
