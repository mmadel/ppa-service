package com.cob.ppa.interceptor;

import com.cob.ppa.service.monitor.RequestMonitorService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class ImportingInterceptor implements HandlerInterceptor {
    @Autowired
    RequestMonitorService requestMonitorService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isImportRequest(request)) {
            String userName =getUsernameFromRequest();
            String requestId = UUID.randomUUID().toString();
            request.setAttribute("pmrbId", requestId);
            MDC.put("pmrbId", requestId); // For logging
            requestMonitorService.createLog(requestId, userName);
        }
        return true;
    }

    private boolean isImportRequest(HttpServletRequest request) {
        return "IMPORT".equalsIgnoreCase(request.getHeader("X-Request-Tag"));
    }
    private String getUsernameFromRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
