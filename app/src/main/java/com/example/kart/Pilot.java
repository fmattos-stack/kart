package com.example.kart;

public class Pilot {

    private String id;
    private String name;
    private int points;
    private int rank;

    public Pilot(){

    }

    public Pilot(String name){
        this.setName(name);
        this.setPoints(0);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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

    @Override
    public String toString(){
        return rank + " - " + name + " - " + points + " - ";
    }


}



