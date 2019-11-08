package vn.pops.account.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import vn.pops.account.security.SecurityUtils;
import vn.pops.account.service.TaskService;
import vn.pops.account.service.client.ClientTaskService;

@RestController
@RequestMapping(value = "/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Autowired
    private TaskService taskService;
    
    @GetMapping(value = "/test")
    public ResponseEntity<String> test() {
        log.info("Test request");
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    
    @GetMapping(value = "/tasks")
    public ResponseEntity<List<?>> getTasks() {
        log.info("Test request: ");
        return new ResponseEntity<List<?>>(taskService.getTasksSync(), HttpStatus.OK);
    }
    
    @GetMapping(value = "/tasks-stats-test")
	public ResponseEntity<Map> getTasksAndStatsTEst(
			@RequestParam(name = "taskFail", required = false, defaultValue = "true") boolean taskFail,
			@RequestParam(name = "statsFail", required = false, defaultValue = "true") boolean statsFail)
			throws InterruptedException, ExecutionException {
        log.info("Test request: ");
        String jwt = "Bearer " + SecurityUtils.getCurrentUserJWT().get();
        CompletableFuture<List> tasks = taskService.getTasks(taskFail);
        CompletableFuture<Map> stats = taskService.getStats(jwt, statsFail);
        Map resp = new HashMap();
        resp.put("tasks", tasks.get());
        resp.put("stats", stats.get());
        return new ResponseEntity<Map>(resp, HttpStatus.OK);
    }
    

    @GetMapping(value = "/tasks-stats")
	public ResponseEntity<Map> getTasksAndStats(
			@RequestParam(name = "taskFail", required = false, defaultValue = "true") boolean taskFail,
			@RequestParam(name = "statsFail", required = false, defaultValue = "true") boolean statsFail,
			@RequestParam(name = "id", required = false) String id)
			throws InterruptedException, ExecutionException {
        log.info("Test request: ");
        CompletableFuture<List> tasks = taskService.getTasks(taskFail);
        CompletableFuture<Map> stats = taskService.getAnalytic(id);
        Map resp = new HashMap();
        resp.put("tasks", tasks.get());
        resp.put("stats", stats.get());
        return new ResponseEntity<Map>(resp, HttpStatus.OK);
    }
    

}
