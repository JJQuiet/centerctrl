package com.rongpan.centerctrl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CenterctrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CenterctrlApplication.class, args);
    }

}
