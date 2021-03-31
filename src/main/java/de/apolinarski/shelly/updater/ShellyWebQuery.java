package de.apolinarski.shelly.updater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.apolinarski.shelly.updater.json.Shelly;
import de.apolinarski.shelly.updater.json.ShellyUpdating;
import de.apolinarski.shelly.updater.json.firmware.AvailableFirmware;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ShellyWebQuery {

    private static final int TIMEOUT_IN_MS = 2000;
    private static final String SHELLY_FIRMWARE_URL = "https://api.shelly.cloud/files/firmware";

    @NonNull
    private final String serverPort;

    @NonNull
    private final WebClient webClient;
    @NonNull
    private final ObjectMapper mapper;

    public ShellyWebQuery(@Value("${server.port}") String serverPort) {
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
        this.serverPort = serverPort;
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

    public AvailableFirmware getFirmwareInformation() {
        WebClient.UriSpec<WebClient.RequestBodySpec> spec = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec bSpec = spec.uri(SHELLY_FIRMWARE_URL);
        try {
            String response = bSpec.accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve().bodyToMono(String.class).block();
            log.trace("Response is {}", response);
            return  mapper.readValue(response, AvailableFirmware.class);
        } catch (JsonProcessingException | WebClientRequestException | WebClientResponseException e) {
            log.warn("Could not resolve firmware information from shelly API: {}",e.getMessage());
            log.trace(e.getMessage(), e);
            AvailableFirmware result = new AvailableFirmware();
            result.setIsok(false);
            return result;
        }
    }

    public ShellyUpdating requestFirmwareUpdate(Shelly shelly, String version) {
        String hostAddress = retrieveHostIpAndPort(shelly.getIp());
        if(hostAddress == null) {
            return new ShellyUpdating();
        }
        WebClient.UriSpec<WebClient.RequestBodySpec> spec = webClient.method(HttpMethod.GET);
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(hostAddress)
                .append("/")
                .append(shelly.getType())
                .append("/")
                .append(version)
                .append("/firmware.zip");
        String firmwareUrl = sb.toString();
        log.debug("Firmware URL is: {}",firmwareUrl);
        WebClient.RequestBodySpec bSpec = spec.uri(builder -> builder.scheme("http").host(shelly.getIp()).path("/ota")
                .queryParam("url", firmwareUrl).build());
        log.debug("Trying shelly ip {}", shelly.getIp());
        try {
            String response = bSpec.accept(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve().bodyToMono(String.class).block();
            return mapper.readValue(response, ShellyUpdating.class);
        } catch (JsonProcessingException | WebClientRequestException | WebClientResponseException e) {
            log.debug("Could not read response, returning empty object: {}",e.getMessage());
            log.trace(e.getMessage(), e);
            return new ShellyUpdating();
        }
    }
    private String retrieveHostIpAndPort(String remoteIp) {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.connect(InetAddress.getByName(remoteIp), 0);
            System.out.println(udpSocket.getLocalAddress());
            NetworkInterface n = NetworkInterface.getByInetAddress(udpSocket.getLocalAddress());
            if (n != null && n.getInetAddresses() != null) {
                InetAddress interfaceAddress = n.getInetAddresses().nextElement();
                if (interfaceAddress != null) {
                    return interfaceAddress.getHostAddress() + ":" + serverPort;
                }
            }
        } catch(SocketException | UnknownHostException e) {
            log.warn("Could not resolve host, cannot update {}\nError message was: {}", remoteIp, e.getMessage());
            log.trace("Caught exception while trying to resolve host address.",e);
        }
        return null;
    }

}
