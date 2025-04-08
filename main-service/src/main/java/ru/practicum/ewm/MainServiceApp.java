package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MainServiceApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApp.class, args);
        //StatsClient client = context.getBean(StatsClient.class);
        //client.postHit(new EndpointHitDto());
        //ResponseEntity<Object> stat = client.getStats(new String(), new String(), List.of(), false);
    }

}
