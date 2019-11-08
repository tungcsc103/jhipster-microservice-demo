package vn.pops.analyticservice.web.rest;

import vn.pops.analyticservice.service.AnalyticServiceKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/analytic-service-kafka")
public class AnalyticServiceKafkaResource {

    private final Logger log = LoggerFactory.getLogger(AnalyticServiceKafkaResource.class);

    private AnalyticServiceKafkaProducer kafkaProducer;

    public AnalyticServiceKafkaResource(AnalyticServiceKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
