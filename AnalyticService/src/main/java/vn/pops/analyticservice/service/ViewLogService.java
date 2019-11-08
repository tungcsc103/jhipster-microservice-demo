package vn.pops.analyticservice.service;

import vn.pops.analyticservice.domain.ViewLog;
import vn.pops.analyticservice.dto.EventLogDTO;
import vn.pops.analyticservice.repository.ViewLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link ViewLog}.
 */
@Service
public class ViewLogService {

    private final Logger log = LoggerFactory.getLogger(ViewLogService.class);

    private final ViewLogRepository viewLogRepository;
    private final AnalyticServiceKafkaProducer kafkaProducer;
    
    private static final ObjectMapper mapper = new ObjectMapper();

    public ViewLogService(ViewLogRepository viewLogRepository, final AnalyticServiceKafkaProducer kafkaProducer) {
        this.viewLogRepository = viewLogRepository;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Save a viewLog.
     *
     * @param viewLog the entity to save.
     * @return the persisted entity.
     */
    public ViewLog save(ViewLog viewLog) {
        log.debug("Request to save ViewLog : {}", viewLog);
        ViewLog result =  viewLogRepository.save(viewLog);
        // TODO: MapStruct to dto
        EventLogDTO dto = new EventLogDTO();
        dto.setId(result.getId());
        dto.setUserId(result.getUserId());
        dto.setEntityType(result.getEntityType());
        dto.setEntityId(result.getEntityId());
        dto.setTime(result.getDate().toEpochSecond());
        
        
        String logMessage;
		try {
			logMessage = mapper.writeValueAsString(dto);
			kafkaProducer.sendViewLogMessage(logMessage);
		} catch (JsonProcessingException e) {
			log.error("Error serialize data : {}", viewLog);
		}
        
        return result;
    }

    /**
     * Get all the viewLogs.
     *
     * @return the list of entities.
     */
    public List<ViewLog> findAll() {
        log.debug("Request to get all ViewLogs");
        return viewLogRepository.findAll();
    }


    /**
     * Get one viewLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ViewLog> findOne(UUID id) {
        log.debug("Request to get ViewLog : {}", id);
        return viewLogRepository.findById(id);
    }

    /**
     * Delete the viewLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ViewLog : {}", id);
        viewLogRepository.deleteById(id);
    }
}
