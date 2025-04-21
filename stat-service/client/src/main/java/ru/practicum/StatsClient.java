package ru.practicum;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    //private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Formatter.DATE_FORMAT);

    //@Value("${server.application.name:ewm-main-service}")
    private String applicationName;

    /*@Value("${server.url}")
    private String serverUrl;*/

    @Autowired
    public StatsClient(@Value("${stats-server.url}")String serverUrl, String applicationName, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.applicationName = applicationName;
    }

    public ResponseEntity<Object> postHit(EndpointHitDto endpointHitDto) {
        log.info("PostHit with endpointHitDto {}", endpointHitDto);
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, @Nullable List<String> uris, Boolean unique) {
        log.info("GetStats with start {}, end {}, uris {}, unique {}", start, end, uris, unique);
        /*Map<String, Object> parameters = Map.of(
                "start", Objects.requireNonNull(start).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", Objects.requireNonNull(end).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "uris", String.join(",", Objects.requireNonNull(uris)),
                "unique", unique
        );*/
        Map<String, Object> parameters;

        if (Objects.isNull(uris)) {
            parameters = Map.of("start", URLEncoder.encode(start, StandardCharsets.UTF_8),
                    "end", URLEncoder.encode(end, StandardCharsets.UTF_8),
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }

        parameters = Map.of("start", URLEncoder.encode(start, StandardCharsets.UTF_8),
                "end", URLEncoder.encode(end, StandardCharsets.UTF_8),
                "uris", String.join(",", uris),
                "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
