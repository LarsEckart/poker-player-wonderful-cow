package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    static final String VERSION = "raise when chen above 10";

    public static int betRequest(GameState gameState) {
        if (gameState.me().score() > 10) {
            return gameState.toRaiseByBlinds(2);
        }
        return 0;
    }

    public static void showdown(JsonNode game) {
    }
}
