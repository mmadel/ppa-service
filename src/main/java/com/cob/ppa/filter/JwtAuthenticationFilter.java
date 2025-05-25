package com.cob.ppa.filter;

import com.cob.ppa.util.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            Claims claims = JwtTokenUtil.extractAllClaims(token);

            if (claims.getSubject() != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (JwtTokenUtil.validateToken(token)) {
                    Object rolesClaim = claims.get("roles");

                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (rolesClaim instanceof List<?>) {
                        for (Object role : (List<?>) rolesClaim) {
                            if (role instanceof String) {
                                authorities.add(new SimpleGrantedAuthority((String) role));
                            }
                        }
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    claims.getSubject(),
                                    null,
                                    authorities);

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    MDC.put("username", authentication.getName());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}