package vn.pops.account.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import vn.pops.account.service.client.ClientAnalyticService;
import vn.pops.account.service.client.ClientStatsService;
import vn.pops.account.service.client.ClientTaskService;

@Service
public class TaskService {

	@Autowired
    private ClientTaskService clientTaskService;
	
	@Autowired
    private ClientStatsService clientStatsService;
	
	@Autowired
	private ClientAnalyticService clientAnalyticService;
	
	public List<?> getTasksSync(){
		return clientTaskService.getTasks(false);
	}
	
	@Async(value = "taskExecutor")
	public CompletableFuture<List> getTasks(boolean taskFail){
		List<?> result = clientTaskService.getTasks(taskFail);
		return CompletableFuture.completedFuture(result);
	}
	
	@Async(value = "taskExecutor")
	public CompletableFuture<Map> getStats(String jwt, boolean statsFail) {
		Map result = clientStatsService.getStats(jwt, statsFail);
		return CompletableFuture.completedFuture(result);
	}
	
	@Async(value = "taskExecutor")
	public CompletableFuture<Map> getAnalytic(String id) {
		Map result = clientAnalyticService.getStats(id);
		return CompletableFuture.completedFuture(result);
	}
}
