package com.yhh.springboot_hbase_ifc.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CorsConfig implements WebMvcConfigurer {
     @Override
     public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**")
                 // 表明允许哪些域访问, 简单点可为 *
                 .allowedOrigins("http://localhost:5000")
                 .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                 .allowedHeaders("*")
                 .allowedMethods("*")
                 // allowCredentials(true): 表示附带身份凭证
                 // 一旦使用 allowCredentials(true) 方法，则 allowedOrigins("*") 需要指明特定的域，而不能是 *
                 .allowCredentials(true)
                 .maxAge(86400);
     }
}
