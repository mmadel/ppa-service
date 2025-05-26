package com.cob.ppa.filter;

import com.cob.ppa.service.monitor.RequestMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    @Autowired
    RequestMonitorService requestMonitorService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String username = getUsernameFromRequest(request);
            String requestInfo = String.format(
                    "%s %s from %s [user: %s] - Status: %d - Duration: %dms",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    username != null ? username : "anonymous",
                    response.getStatus(),
                    System.currentTimeMillis() - startTime
            );
            logger.info(requestInfo);
        }

    }
    private String getUsernameFromRequest(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
