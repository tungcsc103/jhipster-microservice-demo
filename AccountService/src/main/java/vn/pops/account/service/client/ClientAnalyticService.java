package vn.pops.account.service.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import vn.pops.account.config.ClientConfiguration;
import vn.pops.account.service.client.fallback.ClientAnalyticFallback;

@FeignClient(name = "analyticservice", configuration = ClientConfiguration.class, fallback = ClientAnalyticFallback.class)
public interface ClientAnalyticService {

	@GetMapping("/api/view-stats/{id}")
	public Map getStats(@PathVariable(name = "id") String id);
}
