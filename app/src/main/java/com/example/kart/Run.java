package com.example.kart;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Run {

    private String id;
    private String date;
    private Map<String, Integer> rank;
    //constructors
    public Run(){ this.rank = new HashMap<>(); }
    //getters
    public String getId() { return id; }
    public String getDate() { return date; }
    public Map<String, Integer> getRank() { return rank; }
    //setters
    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setRank(ArrayList<String> rank){
        int[] table = {35,30,27,25,23,21,19,17,15,13,12,11,10,9,8};
        int index = 0;
        if (rank != null) {
            for (String pilotName : rank) {
                if (index < table.length) {
                    this.rank.put(pilotName, table[index]);
                } else {
                    this.rank.put(pilotName, 0);
                }
                index++;
            } //for
        }
    }
    //customs
    public int catPilotPoint(@NotNull Pilot pilot){
        for (Map.Entry<String, Integer> mapped : rank.entrySet()) {
            if(mapped.getKey().equals(pilot.getName())){
                return mapped.getValue();
            }
        }
        return -1;
    }
}
