package com.welly.simplypay.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {
    private static final Logger apiLogger = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            String method = requestWrapper.getMethod();
            String uri = requestWrapper.getRequestURI();
            int status = responseWrapper.getStatus();

            // Log the summary
            apiLogger.info("FINISHED: {} {} | Status: {} | Time: {}ms", method, uri, status, duration);

            // Log the Request Body (if any)
            String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!requestBody.isEmpty()) {
                String maskedRequest = maskSensitiveData(requestBody);
                apiLogger.debug("Request Body: {}", maskedRequest);
            }

            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!responseBody.isEmpty()) {
                String maskedResponse = maskSensitiveData(responseBody);
                apiLogger.debug("RESPONSE: Status {} | Body: {}", responseWrapper.getStatus(), maskedResponse);

            }

            //Copy content back to the real response so the user actually gets the data
            responseWrapper.copyBodyToResponse();
        }
    }

    private String maskSensitiveData(String body) {
        if (body == null || body.isEmpty()) return body;

        // Use RegEx to find "number":"1234..." and "cvv":123 and replace values
        String maskedBody = body.replaceAll("\"number\"\\s*:\\s*\"(\\d{6})\\d+(\\d{4})\"", "\"number\":\"$1******$2\"");
        maskedBody = maskedBody.replaceAll("\"cvv\"\\s*:\\s*\\d+", "\"cvv\":\"***\"");

        return maskedBody;
    }
}
