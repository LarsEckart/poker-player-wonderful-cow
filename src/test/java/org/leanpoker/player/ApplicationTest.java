package org.leanpoker.player;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

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
        String body = Map.of("action", "bet_request", "game_state", """
                {
                  "tournament_id": "653ff90bed18aa0002fd9ce6",
                  "game_id": "65400b47c6df840002161fc9",
                  "round": 0,
                  "players": [
                    {
                      "name": "Darth Raiser",
                      "stack": 996,
                      "status": "active",
                      "bet": 4,
                      "time_used": 0,
                      "version": "Demo player",
                      "id": 0
                    },
                    {
                      "name": "Bluffy the Vampire",
                      "stack": 1000,
                      "status": "folded",
                      "bet": 0,
                      "time_used": 212991,
                      "version": "Demo player",
                      "id": 1
                    },
                    {
                      "name": "Winnie the Pot",
                      "stack": 1000,
                      "status": "folded",
                      "bet": 0,
                      "time_used": 215837,
                      "version": "Demo player",
                      "id": 2
                    },
                    {
                      "name": "Wonderful Cow",
                      "stack": 998,
                      "status": "active",
                      "bet": 2,
                      "hole_cards": [
                        {
                          "rank": "10",
                          "suit": "spades"
                        },
                        {
                          "rank": "9",
                          "suit": "diamonds"
                        }
                      ],
                      "time_used": 0,
                      "version": "always raise",
                      "id": 3
                    }
                  ],
                  "small_blind": 2,
                  "big_blind": 4,
                  "orbits": 0,
                  "dealer": 2,
                  "community_cards": [],
                  "current_buy_in": 4,
                  "pot": 6,
                  "in_action": 3,
                  "minimum_raise": 2,
                  "bet_index": 4
                }""").entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
        System.out.println(body);
        HttpRequest request =
                HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .uri(URI.create(server.getURI().toString() ))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(httpResponse.statusCode()).isEqualTo(200);
        assertThat(httpResponse.body()).isEqualTo("0");
    }
}
