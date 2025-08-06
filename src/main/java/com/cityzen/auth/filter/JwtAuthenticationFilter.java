package com.cityzen.auth.filter;

import com.cityzen.auth.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        // Skip public auth endpoints entirely
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (logger.isDebugEnabled()) {
                logger.debug("No Authorization header for request {}", path);
            }
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username;
        try {
            username = jwtUtil.extractUsername(jwt);
        } catch (JwtException ex) {
            logger.warn("Invalid JWT on request {} : {}", path, ex.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
            return;
        } catch (Exception ex) {
            logger.error("Unexpected error extracting username from JWT: {}", ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token processing error");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                var userDetails = userDetailsService.loadUserByUsername(username);

                boolean valid = jwtUtil.validateToken(jwt, userDetails.getUsername());
                if (valid) {
                    var token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                } else {
                    logger.debug("JWT validation failed for user {}", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
                    return;
                }
            } catch (JwtException ex) {
                logger.warn("JWT validation exception for request {} : {}", path, ex.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
                return;
            } catch (Exception ex) {
                logger.error("Error during user lookup or JWT validation: {}", ex.getMessage(), ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Auth processing error");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
