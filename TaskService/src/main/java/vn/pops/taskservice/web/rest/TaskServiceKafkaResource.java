package vn.pops.taskservice.web.rest;

import vn.pops.taskservice.service.TaskServiceKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/task-service-kafka")
public class TaskServiceKafkaResource {

    private final Logger log = LoggerFactory.getLogger(TaskServiceKafkaResource.class);

    private TaskServiceKafkaProducer kafkaProducer;

    public TaskServiceKafkaResource(TaskServiceKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
