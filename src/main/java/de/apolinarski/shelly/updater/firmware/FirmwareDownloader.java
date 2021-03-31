package de.apolinarski.shelly.updater.firmware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.apolinarski.shelly.updater.json.Shelly;
import de.apolinarski.shelly.updater.json.firmware.AvailableFirmware;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
public class FirmwareDownloader {

    private static final int TIMEOUT_IN_MS = 2000;

    @NonNull
    private final String firmwareFileUrl;
    @NonNull
    private final Path targetDirectory;
    @NonNull
    private final WebClient webClient;

    public FirmwareDownloader(String firmwareFileUrl, Path targetDirectory) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_IN_MS)
                .responseTimeout(Duration.ofMillis(TIMEOUT_IN_MS))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS))
                );
        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        this.firmwareFileUrl = firmwareFileUrl;
        this.targetDirectory = targetDirectory;
    }

    public void downloadFile() {
        WebClient.UriSpec<WebClient.RequestBodySpec> spec = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec bSpec = spec.uri(firmwareFileUrl);
        try {
            Flux<DataBuffer> bufferFlux = bSpec.accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve().bodyToFlux(DataBuffer.class);
            DataBufferUtils.write(bufferFlux, targetDirectory, StandardOpenOption.CREATE).block();
        } catch (WebClientRequestException | WebClientResponseException e) {
            log.debug("Trying: {}", firmwareFileUrl);
            log.debug("Could not read response, returning empty object.");
            log.trace(e.getMessage(), e);
        }
    }

}
