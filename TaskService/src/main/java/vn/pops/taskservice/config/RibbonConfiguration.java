package vn.pops.taskservice.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;

@RibbonClient(name = "taskservice")
public class RibbonConfiguration {
	@Bean
	public IRule loadBlancingRule() {
		return new RoundRobinRule();
	}
}