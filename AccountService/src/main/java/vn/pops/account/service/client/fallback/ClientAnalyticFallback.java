package vn.pops.account.service.client.fallback;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;

import vn.pops.account.service.client.ClientAnalyticService;

@Component
public class ClientAnalyticFallback implements ClientAnalyticService{

	@Override
	public Map getStats(String id) {
		return Collections.emptyMap();
	}

}
