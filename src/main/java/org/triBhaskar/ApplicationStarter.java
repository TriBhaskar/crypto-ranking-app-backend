package org.triBhaskar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.triBhaskar.utils.Utility;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class ApplicationStarter {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}