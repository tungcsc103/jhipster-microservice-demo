package vn.pops.analyticservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalyticServiceKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(AnalyticServiceKafkaProducer.class);
    private static final String TOPIC = "topic_analyticservice";
    private static final String VIEW_LOG_TOPIC = "topic_viewlog";

    private KafkaTemplate<String, String> kafkaTemplate;

    public AnalyticServiceKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Producing message to {} : {}", TOPIC, message);
        this.kafkaTemplate.send(TOPIC, message);
    }
    
    public void sendViewLogMessage(String message) {
        log.info("Producing message to {} : {}", VIEW_LOG_TOPIC, message);
        this.kafkaTemplate.send(VIEW_LOG_TOPIC, message);
    }
}
