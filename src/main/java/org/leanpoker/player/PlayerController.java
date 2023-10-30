package org.leanpoker.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Controller()
public class PlayerController {

    private static final Logger log = getLogger(PlayerController.class);

    public ObjectMapper mapper = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build();

    @Get(produces = MediaType.TEXT_PLAIN)
    public String doGet() {
        return "Java player is running";
    }

    @Post(produces = MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String doPost(@Body Map<String, String> body) throws JsonProcessingException {
        String action = body.get("action");
        String gameState = body.get("game_state");
        if ("bet_request".equals(action)) {
            log.info(gameState);
            return String.valueOf(Player.betRequest(mapper.readValue(gameState, GameState.class)));
        }
        if ("showdown".equals(action)) {
            Player.showdown(mapper.readTree(gameState));
        }
        if ("version".equals(action)) {
            return Player.VERSION;
        }
        return "";
    }

}
