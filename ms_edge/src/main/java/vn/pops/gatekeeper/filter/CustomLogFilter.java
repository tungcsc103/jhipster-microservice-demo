package vn.pops.gatekeeper.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import brave.Span;
import brave.Tracer;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 6)
public class CustomLogFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(CustomLogFilter.class);

	private static final String SESSION_HEADER = "X-Session-Id";
	private static final String TRACE_HEADER = "X-Trace-Id";

	@Autowired
	private Tracer tracer;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String sessionId = request.getHeader(SESSION_HEADER);
		if(StringUtils.isBlank(sessionId)) {
			sessionId = UUID.randomUUID().toString();
		}
		log.info("Session ID: {}", sessionId);
        Span currentSpan = this.tracer.currentSpan();

		response.addHeader(TRACE_HEADER, currentSpan.context().traceIdString());
		currentSpan.tag("sessionId", sessionId);
		filterChain.doFilter(request, response);
	}

}
