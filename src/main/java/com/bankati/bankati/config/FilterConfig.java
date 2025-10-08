package com.bankati.bankati.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.io.IOException;
import java.util.Set;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> charsetEncodingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Filter() {
            private static final String ENCODING = "UTF-8";

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                request.setCharacterEncoding(ENCODING);
                response.setCharacterEncoding(ENCODING);
                chain.doFilter(request, response);
            }
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("charsetEncodingFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> ipRestrictionFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Filter() {
            private final Set<String> ALLOWED_IPS = Set.of("127.0.0.1", "192.168.1.100");

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                String clientIp = request.getRemoteAddr();
                if (!ALLOWED_IPS.contains(clientIp)) {
                    ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Access denied for IP: " + normalizeIp(clientIp));
                    System.out.println("Access denied for IP: " + normalizeIp(clientIp));
                    return;
                }
                chain.doFilter(request, response);
            }

            private String normalizeIp(String ip) {
                return ip.equals("0:0:0:0:0:0:0:1") ? "LocalHost [127.0.0.1]" : ip;
            }
        });
        registrationBean.addUrlPatterns("/public/*");
        registrationBean.setName("ipRestrictionFilter");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> requestLoggingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletRequest req = (HttpServletRequest) request;
                System.out.println("➡ [LOG] Requête : " + req.getMethod() + " " + req.getRequestURI());
                chain.doFilter(request, response);
            }
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("requestLoggingFilter");
        registrationBean.setOrder(3);
        return registrationBean;
    }
}
