package vn.pops.statsservive.web.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.undertow.util.BadRequestException;
import vn.pops.statsservive.service.StatsService;

@RestController
@RequestMapping(value = "/api")
public class StatsResource {

    private final Logger log = LoggerFactory.getLogger(StatsResource.class);

    @Autowired
    private StatsService statsService;
    
    @GetMapping(value = "/stats")
    public ResponseEntity<Map<String, Long>> getStats(@RequestParam(name = "statsFail") boolean statsFail) throws BadRequestException {
        log.info("Get stats for current user");
        if(statsFail) {
        	throw new BadRequestException("Testing: Error processing");
        }
        return new ResponseEntity<Map<String, Long>>(statsService.getStats(), HttpStatus.OK);
    }
}
