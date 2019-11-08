package vn.pops.account.service.client.fallback;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;

import vn.pops.account.service.client.ClientStatsService;

@Component
public class ClientStatsFallback implements ClientStatsService{

	@Override
	public Map getStats(String jwt, boolean fail) {
		return Collections.emptyMap();
	}

}
