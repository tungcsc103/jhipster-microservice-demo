package vn.pops.statsservive.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.pops.statsservive.service.dto.EventLogDTO;
import vn.pops.statsservive.service.dto.UserStatsDTO;

@Service
public class StatsService {
	
	private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    private UserStatsService userStatsService;
    
	public Map<String, Long> getStats() {
		Map<String, Long> stats = new HashMap<>();
		stats.put("view", 200000L);
		stats.put("like", 3200L);
		return stats;
	}
	
	public void updateView(String message) {
		try {
			EventLogDTO dto = mapper.readValue(message, EventLogDTO.class);
			Optional<UserStatsDTO> userOpt = userStatsService.findOneByUserId(dto.getUserId());
			if(userOpt.isPresent()) {
				UserStatsDTO user = userOpt.get();
				log.info("User found: {}", user);
				Long totalView = user.getTotalView();
				totalView = totalView == null ? 0L : totalView;
				user.setTotalView(totalView + 1);
				userStatsService.save(user);
			}
			log.info("Serialize data: {}", dto);
		} catch (IOException e) {
			log.error("Error when deserialize event log ");
		}
	}
}
