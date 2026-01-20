package com.example.demo.adapter.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * The Class MvcConfig.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * View resolver.
     *
     * @return the internal resource view resolver
     */
    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");  // coincide con webapp/WEB-INF/views/
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
