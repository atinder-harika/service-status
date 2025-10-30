package com.atinder.service_status_backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CORS filter to ensure CORS headers are added to all responses.
 * This works alongside CorsConfig for maximum compatibility with Railway/Render.
 * 
 * High priority order ensures CORS headers are added before other filters.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        
        // Allow requests from GitHub Pages and localhost
        String origin = request.getHeader("Origin");
        if (origin != null && (
                origin.equals("https://atinder-harika.github.io") ||
                origin.startsWith("http://localhost")
        )) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            // Default to GitHub Pages if no origin or unknown origin
            response.setHeader("Access-Control-Allow-Origin", "https://atinder-harika.github.io");
        }
        
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // Handle preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        chain.doFilter(req, res);
    }
}
