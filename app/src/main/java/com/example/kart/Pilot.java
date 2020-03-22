package com.example.kart;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Pilot implements  Comparable<Pilot>{

    private String id;
    private String name;
    private int total_points;
    private int rank;
    private int runs;

    public Pilot(){
    }
    public Pilot(String name){
        this.setName(name);
        this.setTotal_points(0);
        this.setRank(0);
        this.setRuns(0);
    }
    //getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }
    public int getTotal_points() {
        return total_points;
    }
    public int getRank() {
        return rank;
    }
    public int getRuns() {
        return runs;
    }
    //setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTotal_points(int point) {
        this.total_points += point;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public void setRuns(int runs) {
        this.runs += runs;
    }
    //customs
    public void delTotalPoints(int point){
        this.total_points -= point;
    }
    public String rowTable(){
        return String.format("%d - %s - %d", this.rank, this.name, this.total_points);
    }
    public void sort(ArrayList<Pilot> pilots){
        Collections.sort(pilots);
        int position = 1;
        for(Pilot pilot : pilots) {
            if(pilot.getName().equals(this.name)) {
                pilot.setRank(position);
            }
            position++;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(@NotNull Pilot o) {
        return Integer.compare(this.total_points, o.total_points);
    }
}



