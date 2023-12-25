package com.project.consumer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Counter {

    private Long total;
    private Map<String, Map<String, Long>> counter;

    public Counter(Long total, Map<String, Map<String, Long>> counter) {
        this.total = total;
        this.counter = new HashMap<>(counter.size());
        counter.forEach((k, v) -> this.counter.put(k, new HashMap<>(v)));
    }
}
