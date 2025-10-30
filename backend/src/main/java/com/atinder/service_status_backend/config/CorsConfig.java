package com.atinder.service_status_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for frontend-backend communication.
 * Allows frontend (GitHub Pages) to call backend API (Railway/Render).
 * 
 * Note: Using allowedOrigins instead of allowedOriginPatterns for better Railway compatibility.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all endpoints (not just /api/**)
                .allowedOrigins(
                    "http://localhost:5173",
                    "http://localhost:3000",
                    "https://atinder-harika.github.io"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(false)  // Set to false for simpler CORS (no credentials needed)
                .maxAge(3600);
    }
}
