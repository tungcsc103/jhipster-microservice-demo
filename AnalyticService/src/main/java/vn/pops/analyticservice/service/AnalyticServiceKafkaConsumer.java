package vn.pops.analyticservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnalyticServiceKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(AnalyticServiceKafkaConsumer.class);
    private static final String TOPIC = "topic_analyticservice";

    @KafkaListener(topics = "topic_analyticservice", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
