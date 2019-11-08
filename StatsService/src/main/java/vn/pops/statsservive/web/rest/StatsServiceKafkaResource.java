package vn.pops.statsservive.web.rest;

import vn.pops.statsservive.service.StatsServiceKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/stats-service-kafka")
public class StatsServiceKafkaResource {

    private final Logger log = LoggerFactory.getLogger(StatsServiceKafkaResource.class);

    private StatsServiceKafkaProducer kafkaProducer;

    public StatsServiceKafkaResource(StatsServiceKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
