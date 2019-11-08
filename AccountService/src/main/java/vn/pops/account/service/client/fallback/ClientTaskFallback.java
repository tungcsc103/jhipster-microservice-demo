package vn.pops.account.service.client.fallback;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.pops.account.service.client.ClientTaskService;

@Component
public class ClientTaskFallback implements ClientTaskService{


	@Override
	public List<?> getTasks(boolean taskFail) {
		return Collections.emptyList();
	}

}
