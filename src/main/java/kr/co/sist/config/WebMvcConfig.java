package kr.co.sist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 요청이 들어오면 로컬 C:/upload/ 폴더에서 찾음
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/upload/");
    }
}
