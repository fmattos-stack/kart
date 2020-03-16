package com.example.kart;

import java.util.UUID;

public class Pilot {

    private String id;
    private String name;
    private int points;
    private int rank;

    public Pilot(String name){
        this.setName(name);
        this.setPoints(0);
        this.setRank(100);
        this.setId();
    }

    public String getId() {
        return id;
    }

    private void setId() {
        String id = UUID.randomUUID().toString();
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
           this.name = name;
    }

}

