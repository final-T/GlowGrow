package com.tk.gg.multimedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableFeignClients
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {"com.tk.gg", "com.tk.gg.common"})
public class MultimediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultimediaApplication.class, args);
    }

}
