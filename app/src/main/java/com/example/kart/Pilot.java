package com.example.kart;

import java.util.ArrayList;

public class Pilot {

    private String id;
    private String name;
    private int total_points;
    private int rank;
    private int runs;

    public Pilot(){}

    public Pilot(String name){
        this.setName(name);
        this.setTotal_points(0);
        this.setRank(0);
        this.setRuns(0);
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int point) {
        this.total_points += point;
    }

    public void delTotalPoints(int point){
        this.total_points -= point;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
        return rank + " - " + name + " - " + total_points;
    }


}



