package ru.practicum;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service(value = "statsClient")
@Slf4j
public class StatsClient extends BaseClient {

    private String applicationName;

    public StatsClient(@Value("${stats-server.url}") String serverUrl, @Value("${application.name}") String applicationName, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.applicationName = applicationName;
    }

    public ResponseEntity<Object> postHit(EndpointHitDto endpointHitDto) {
        log.info("PostHit with endpointHitDto {} в клиенте", endpointHitDto);
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, @Nullable List<String> uris, Boolean unique) {
        log.info("GetStats with start {}, end {}, uris {}, unique {}", start, end, uris, unique);
        Map<String, Object> parameters;

        if (Objects.isNull(uris)) {
            parameters = Map.of("start", start,
                    "end", end,
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }

        parameters = Map.of("start", start,
                "end", end,
                "uris", String.join(",", uris),
                "unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
