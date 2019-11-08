package vn.pops.account.web.rest;

import vn.pops.account.service.AccountServiceKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/account-service-kafka")
public class AccountServiceKafkaResource {

    private final Logger log = LoggerFactory.getLogger(AccountServiceKafkaResource.class);

    private AccountServiceKafkaProducer kafkaProducer;

    public AccountServiceKafkaResource(AccountServiceKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.info("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
