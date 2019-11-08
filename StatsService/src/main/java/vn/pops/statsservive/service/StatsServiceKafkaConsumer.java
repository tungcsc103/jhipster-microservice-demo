package vn.pops.statsservive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StatsServiceKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(StatsServiceKafkaConsumer.class);
    private static final String TOPIC = "topic_statsservice";
    private static final String VIEW_LOG_TOPIC = "topic_viewlog";
    
    @Autowired
    private StatsService statsService;

    @KafkaListener(topics = "topic_statsservice", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
    
    @KafkaListener(topics = VIEW_LOG_TOPIC, groupId = "group_id")
    public void consumeViewLog(String message) throws IOException {
        log.info("Consumed message in {} : {}", VIEW_LOG_TOPIC, message);
        statsService.updateView(message);
    }
}
