package org.leanpoker.player;

public record CardCount(int community, int hole) {

    public int total() {
        return community + hole;
    }
}
