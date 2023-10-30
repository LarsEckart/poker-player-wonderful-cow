package org.leanpoker.player;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class ApplicationTest {

    @Inject
    EmbeddedServer server;

    @Test
    void getRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest.newBuilder()
                        .GET()
                        .uri(server.getURI())
                        .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.statusCode()).isEqualTo(200);
        assertThat(httpResponse.body()).isEqualTo("Java player is running");
    }

    @Test
    void postRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String body = Map.of("action", "version").entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).reduce((a, b) -> a + "&" + b).orElse("");
        System.out.println(body);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .uri(URI.create(server.getURI().toString() ))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.statusCode()).isEqualTo(200);
        assertThat(httpResponse.body()).isEqualTo("always raise");
    }
}
