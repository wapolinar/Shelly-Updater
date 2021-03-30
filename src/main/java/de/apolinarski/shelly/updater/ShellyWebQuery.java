package de.apolinarski.shelly.updater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.apolinarski.shelly.updater.json.Shelly;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShellyWebQuery {

    private static final int TIMEOUT_IN_MS = 2000;

    private final WebClient webClient;
    private final ObjectMapper mapper;

    public ShellyWebQuery() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_IN_MS)
                .responseTimeout(Duration.ofMillis(TIMEOUT_IN_MS))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS))
                );

        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Shelly createRequest(String shellyAddress) {
        WebClient.UriSpec<WebClient.RequestBodySpec> spec = webClient.method(HttpMethod.GET);
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(shellyAddress)
                .append("/shelly");
        WebClient.RequestBodySpec bSpec = spec.uri(sb.toString());
        try {
            String response = bSpec.accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve().bodyToMono(String.class).block();
            Shelly shelly =  mapper.readValue(response, Shelly.class);
            shelly.setIp(shellyAddress);
            return shelly;
        } catch (JsonProcessingException | WebClientRequestException | WebClientResponseException e) {
            log.debug("Trying: {}", sb.toString());
            log.debug("Could not read response, returning empty object.");
            log.trace(e.getMessage(), e);
            return new Shelly();
        }
    }
}
