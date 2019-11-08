package vn.pops.account.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import vn.pops.account.config.ClientConfiguration;
import vn.pops.account.service.client.fallback.ClientTaskFallback;

@FeignClient(name = "taskservice", configuration = ClientConfiguration.class, fallback = ClientTaskFallback.class)
public interface ClientTaskService {
	
	@GetMapping("/api/tasks")
	public List<?> getTasks(@RequestParam(value="taskFail") boolean taskFail);
}
