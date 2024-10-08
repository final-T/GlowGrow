package com.tk.gg.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableFeignClients
@ComponentScan(basePackages = {"com.tk.gg", "com.tk.gg.common"})
public class GlowGrowUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlowGrowUsersApplication.class, args);
    }

}
