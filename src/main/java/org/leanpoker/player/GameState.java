package org.leanpoker.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

record GameState(
        String tournamentId,
        String gameId,
        int round,
        int betIndex,
        int smallBlind,
        int currentBuyIn,
        int pot,
        int minimumRaise,
        int dealer,
        int orbits,
        int inAction,
        List<PlayerState> players,
        List<Card> communityCards) {

    public int myId() {
        return inAction;
    }

    public PlayerState me() {
        return players().get(myId());
    }

    /* Return this value if you want to call */
    public int toCall() {
        return currentBuyIn() - me().bet();
    }

    /* Return this value if you want to make the smallest possible raise */
    public int toRaise() {
        return toCall() + minimumRaise();
    }

    /* Use this function to raise by n times the big blinds */
    public int toRaiseByBlinds(int n) {
        return toRaise() + n * bigBlind();
    }

    public int bigBlind() {
        return smallBlind * 2;
    }

    public int playersCount() {
        return players.size();
    }

    public Map<Suit, CardCount> suitCounts() {
        Map<Suit, CardCount> counts = new HashMap<>();
        for (Suit s : Suit.values()) {
            int holeCount = (int) me().holeCards().stream().filter(c -> c.suit().equals(s)).count();
            int communityCount = (int) communityCards().stream().filter(c -> c.suit().equals(s)).count();
            counts.put(s, new CardCount(communityCount, holeCount));
        }
        return counts;
    }

    public Map<String, CardCount> rankCounts() {
        List<String> ranks = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "D", "K", "A");
        Map<String, CardCount> counts = new HashMap<>();
        for (String r : ranks) {
            int holeCount = (int) me().holeCards().stream().filter(c -> c.rank().equals(r)).count();
            int communityCount = (int) communityCards().stream().filter(c -> c.rank().equals(r)).count();
            counts.put(r, new CardCount(communityCount, holeCount));
        }
        return counts;
    }

    /**
     * Betting position tells you if you are betting early or late.
     * If this value is 1, you are the first to make a bet when there is the
     * least amount of information available.
     * If this value is equal to the number of players, you are the dealer
     * and you make your bet after everyone else.
     */
    public int bettingPosition() {
        return (inAction - dealer + playersCount() - 1) % playersCount() + 1;
    }

    public String bettingRound() {
        return switch (communityCards.size()) {
            case 0 -> "pre flop";
            case 3 -> "flop";
            case 4 -> "turn";
            case 5 -> "river";
            default -> throw new IllegalStateException("Unexpected value: " + communityCards.stream());
        };
    }

    public int activePlayersInGame() {
        return players().stream().filter(p -> p.status() != (PlayerStatus.OUT)).toArray().length;
    }

    public int activePlayersInHand() {
        return players().stream().filter(p -> p.status().equals(PlayerStatus.ACTIVE)).toArray().length;
    }
}
