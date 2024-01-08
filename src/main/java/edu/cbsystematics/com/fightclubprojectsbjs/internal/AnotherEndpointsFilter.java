package edu.cbsystematics.com.fightclubprojectsbjs.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AnotherEndpointsFilter implements Filter {

    private final int anotherPort;
    private final String anotherPathPrefix;
    private final String BAD_REQUEST = String.format("{\"code\":%d,\"error\":true,\"errorMessage\":\"%s\"}",
            HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());

    public AnotherEndpointsFilter(int anotherPort, String anotherPathPrefix) {
        this.anotherPort = anotherPort;
        this.anotherPathPrefix = anotherPathPrefix;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // Checks if the request is targeting an API endpoint and whether it's using the correct another port
        boolean isAnotherAPI = ((HttpServletRequestWrapper) servletRequest).getRequestURI().startsWith(anotherPathPrefix);
        boolean isAnotherPort = servletRequest.getLocalPort() == anotherPort;

        // Denies requests that do not meet the another API endpoint and port criteria
        if ((isAnotherAPI && !isAnotherPort) || (!isAnotherAPI && isAnotherPort)) {
            log.debug("Deny the request");

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                outputStream.write(BAD_REQUEST.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.error("Error writing response: {}", e.getMessage());
            }
            return;
        }

        // Allows the request to pass through the filter chain for other endpoints
        filterChain.doFilter(servletRequest, servletResponse);
    }

}