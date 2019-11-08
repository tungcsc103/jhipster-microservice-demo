package vn.pops.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AccountServiceKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(AccountServiceKafkaConsumer.class);
    private static final String TOPIC = "topic_accountservice";

    @KafkaListener(topics = "topic_accountservice", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
