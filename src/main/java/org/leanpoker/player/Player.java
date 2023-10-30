package org.leanpoker.player;

import com.fasterxml.jackson.databind.JsonNode;

public class Player {

    static final String VERSION = "always raise";

    public static int betRequest(GameState gameState) {

        return gameState.toRaise();
    }

    public static void showdown(JsonNode game) {
    }
}
