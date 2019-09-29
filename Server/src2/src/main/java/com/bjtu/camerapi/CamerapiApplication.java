package com.bjtu.camerapi;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.bjtu.camerapi.mapper")
public class CamerapiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CamerapiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CamerapiApplication.class);
    }

}
