package org.leanpoker.player;

record Card(String rank, Suit suit) {

    public int value() {
        return switch (rank) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 11;
            default -> Integer.parseInt(rank);
        };
    }

    public double score() {
        return switch (rank) {
            case "A" -> 10;
            case "K" -> 8;
            case "Q" -> 7;
            case "J" -> 6;
            default -> value() / 2.0;
        };
    }
}
