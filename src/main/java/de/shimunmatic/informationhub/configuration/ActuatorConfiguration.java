package de.shimunmatic.informationhub.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.Collections;

@Configuration
public class ActuatorConfiguration {

    @Bean
    public FilterRegistrationBean forwardedHeaderFilter() {
        final FilterRegistrationBean<ForwardedHeaderFilter> filter = new FilterRegistrationBean<>(new ForwardedHeaderFilter());
        filter.setName("Forwarded Header filter");
        filter.setUrlPatterns(Collections.singletonList("/actuator/*"));
        return filter;
    }
}
