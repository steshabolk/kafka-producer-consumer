package com.project.producer.service;

import com.project.producer.config.SchedulerProps;
import com.project.producer.dto.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.project.producer.util.Helper.info;

@Service
public class InvocationCounterService {

    private final ReentrantLock lock = new ReentrantLock();
    private Long total = 0L;
    private final Map<String, Map<String, Long>> initial = new HashMap<>();
    private final Map<String, Map<String, Long>> counter = new HashMap<>();

    @Autowired
    public InvocationCounterService(SchedulerProps schedulerProps) {
        for (SchedulerProps.Scheduler scheduler : schedulerProps.getSchedulers()) {
            String topic = scheduler.getTopic();
            Map<String, Long> keys = new HashMap<>();
            for (int i = scheduler.getKeyMin(); i <= scheduler.getKeyMax(); i++) {
                keys.put(String.valueOf(i), 0L);
            }
            initial.put(topic, keys);
            counter.put(topic, new HashMap<>(keys));
        }
    }

    public void countInvocation(String topic, String key) {
        lock.lock();
        try {
            if (total == Long.MAX_VALUE) {
                resetCounter();
            }
            total++;
            Map<String, Long> keys = counter.get(topic);
            keys.put(key, keys.get(key) + 1);
            info(String.format("TOTAL MESSAGES=[%s], topic=[%s], key=[%s], messages=[%s]", total, topic, key, keys.get(key)));
        } finally {
            lock.unlock();
        }
    }

    public Counter getCounter() {
        lock.lock();
        try {
            return new Counter(total, counter);
        } finally {
            lock.unlock();
        }
    }

    public Counter getInitialCounter() {
        return new Counter(0L, initial);
    }

    private void resetCounter() {
        total = 0L;
        counter.forEach((k, v) -> v.forEach((i, j) -> v.put(i, 0L)));
    }
}
