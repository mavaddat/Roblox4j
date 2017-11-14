package online.pizzacrust.roblox.impl;

import online.pizzacrust.roblox.api.group.Roleset;

public class BasicRoleset implements Roleset {
    private final int rankIndex;
    private final int id;
    private final String name;

    public BasicRoleset(int rankIndex, int id, String name) {
        this.rankIndex = rankIndex;
        this.id = id;
        this.name = name;
    }

    @Override
    public int getRankIndex() {
        return rankIndex;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
