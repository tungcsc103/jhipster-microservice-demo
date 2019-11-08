package vn.pops.account.service.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import vn.pops.account.config.ClientConfiguration;
import vn.pops.account.service.client.fallback.ClientStatsFallback;

@FeignClient(name = "statsservice", configuration = ClientConfiguration.class, fallback = ClientStatsFallback.class)
public interface ClientStatsService {

	@GetMapping("/api/stats")
	public Map getStats(@RequestHeader(value = "Authorization") String jwt, @RequestParam(value="statsFail") boolean statsFail);
}
