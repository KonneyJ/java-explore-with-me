package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"ewm", "client"})
public class MainServiceApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApp.class, args);
    }

}
