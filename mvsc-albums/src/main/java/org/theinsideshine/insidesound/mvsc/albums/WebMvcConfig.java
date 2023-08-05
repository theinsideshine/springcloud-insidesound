package org.theinsideshine.insidesound.mvsc.albums;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.theinsideshine.insidesound.mvsc.albums.converter.MultipartFileToByteArrayConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MultipartFileToByteArrayConverter());
    }
}
