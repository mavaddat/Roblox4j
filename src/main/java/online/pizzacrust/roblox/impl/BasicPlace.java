package online.pizzacrust.roblox.impl;

import online.pizzacrust.roblox.Place;

public class BasicPlace implements Place {

    private final int id;
    private final int visits;
    private final String name;
    private final String description;

    public BasicPlace(int id, int visits, String name, String description) {
        this.id = id;
        this.visits = visits;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPlaceVisits() {
        return visits;
    }

    @Override
    public int getId() {
        return id;
    }
}
