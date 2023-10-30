package org.leanpoker.player;

import java.util.List;

record PlayerState(int id, String name, PlayerStatus status, String version, int stack, int bet, List<Card> holeCards) {

    public boolean hasPocketPair() {
        var cards = holeCards();
        return cards.get(0).rank().equals(cards.get(1).rank());
    }

    public boolean hasPocketSuited() {
        var cards = holeCards();
        return cards.get(0).suit().equals(cards.get(1).suit());
    }

    public int highestPocketValue() {
        var cards = holeCards();
        return cards.stream().mapToInt(Card::value).max().orElse(0);
    }

    public int pocketGap() {
        var cards = holeCards();
        return Math.abs(cards.get(0).value() - cards.get(1).value()) - 1;
    }

    /**
     * Score our initial hand. A higher score means a better hand.
     */
    public int score() {
        var cards = holeCards();
        var score = cards.stream().mapToDouble(Card::score).max().orElse(0);

        if (hasPocketPair()) {
            score = Math.max(5, score * 2);
        } else if (pocketGap() < 3) {
            score -= pocketGap();
        } else if (pocketGap() == 3) {
            score -= 4;
        } else {
            score -= 5;
        }

        if (((pocketGap()) == 0 || pocketGap() == 1) && highestPocketValue() < 12) {
            score += 1;
        }

        if (hasPocketSuited()) {
            score += 2;
        }

        return (int) Math.ceil(score);
    }
}

