package com.project.producer.service;

import com.project.producer.config.SchedulerProps;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.project.producer.config.SchedulerProps.Scheduler;
import static com.project.producer.util.Helper.error;
import static com.project.producer.util.Helper.getRandom;
import static com.project.producer.util.Helper.info;

@Service
public class ProducerService {

    private final KafkaTemplate<String , String> kafkaTemplate;
    private final SchedulerProps schedulerProps;
    private final InvocationCounterService invocationCounterService;

    @Autowired
    public ProducerService(KafkaTemplate<String, String> kafkaTemplate, SchedulerProps schedulerProps, InvocationCounterService invocationCounterService) {
        this.kafkaTemplate = kafkaTemplate;
        this.schedulerProps = schedulerProps;
        this.invocationCounterService = invocationCounterService;
    }

    @Async
    @Scheduled(initialDelayString = "${scheduler.schedulers[0].initial-delay}", fixedRateString = "${scheduler.schedulers[0].fixed-rate}")
    public void send_1() {
        Scheduler scheduler = schedulerProps.getSchedulers().get(0);
        sendMessage(scheduler);
    }

    @Async
    @Scheduled(initialDelayString = "${scheduler.schedulers[1].initial-delay}", fixedRateString = "${scheduler.schedulers[1].fixed-rate}")
    public void send_2() {
        Scheduler scheduler = schedulerProps.getSchedulers().get(1);
        sendMessage(scheduler);
    }

    @Async
    @Scheduled(initialDelayString = "${scheduler.schedulers[2].initial-delay}", fixedRateString = "${scheduler.schedulers[2].fixed-rate}")
    public void send_3() {
        Scheduler scheduler = schedulerProps.getSchedulers().get(2);
        sendMessage(scheduler);
    }

    @Async
    @Scheduled(initialDelayString = "${scheduler.schedulers[3].initial-delay}", fixedRateString = "${scheduler.schedulers[3].fixed-rate}")
    public void send_4() {
        Scheduler scheduler = schedulerProps.getSchedulers().get(3);
        sendMessage(scheduler);
    }

    @Async
    @Scheduled(initialDelayString = "${scheduler.schedulers[4].initial-delay}", fixedRateString = "${scheduler.schedulers[4].fixed-rate}")
    public void send_5() {
        Scheduler scheduler = schedulerProps.getSchedulers().get(4);
        sendMessage(scheduler);
    }

    private void sendMessage(Scheduler scheduler) {
        CompletableFuture<SendResult<String, String>> future;
        try {
            future = kafkaTemplate.send(scheduler.getTopic(),
                String.valueOf(getRandom(scheduler.getKeyMin(), scheduler.getKeyMax())),
                String.valueOf(getRandom(scheduler.getValueMin(), scheduler.getValueMax())));
        } catch (Exception e) {
            error(String.format("error when sending a message by scheduler=[%s]", scheduler.getTopic()));
            return;
        }
        future.whenCompleteAsync((result, ex) -> {
            ProducerRecord<String, String> record = result.getProducerRecord();
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                info(String.format("MESSAGE SENT : key=[%s], value=[%s], topic=[%s], partition=[%s], offset=[%s], thread=[%s]",
                        record.key(), record.value(), metadata.topic(), metadata.partition(), metadata.offset(), Thread.currentThread().getName()));
                invocationCounterService.countInvocation(metadata.topic(), record.key());
            } else {
                error(String.format("ERROR SENDING A MESSAGE : key=[%s], value=[%s], topic=[%s]%n exception=[%s]",
                        record.key(), record.value(), record.topic(), ex.getMessage()));
            }
        });
    }
}
