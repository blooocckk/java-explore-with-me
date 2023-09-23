package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StatsClient {

    private final WebClient webClient;

    public StatsClient(WebClient.Builder webClientBuilder, @Value("${stats.uri}") String uri) {
        this.webClient = webClientBuilder.baseUrl(uri).build();
    }


    public Mono<OutputHitDto> saveHit(EndpointHitDto endpointHitDto) {
        return webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(OutputHitDto.class);
    }

    public Mono<List<ViewStats>> getStats(String start,
                                          String end,
                                          List<String> uris,
                                          Boolean unique) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris.toArray())
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStats>>() {
                });
    }
}
