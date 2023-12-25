package com.project.producer.controller;

import com.project.producer.service.InvocationCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController {

    private final InvocationCounterService invocationCounterService;

    @Autowired
    public ProducerController(InvocationCounterService invocationCounterService) {
        this.invocationCounterService = invocationCounterService;
    }

    @GetMapping()
    public ResponseEntity<?> getCount() {
        return new ResponseEntity<>(invocationCounterService.getCounter(), HttpStatus.OK);
    }

    @GetMapping("/topics")
    public ResponseEntity<?> getTopics() {
        return new ResponseEntity<>(invocationCounterService.getInitialCounter(), HttpStatus.OK);
    }
}
