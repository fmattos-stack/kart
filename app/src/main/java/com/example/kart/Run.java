package com.example.kart;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Run {

    private String id;
    private String date;
    //private ArrayList<String> rank;
    private ArrayList<Integer> points;
    private Map<String, Integer> rank;

    public Run(){
        this.rank = new HashMap<>();
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    //public ArrayList<String> getRank() { return rank; }
    //public void setRank(ArrayList<String> rank) { this.rank = rank; }
    @Override
    public String toString(){ return date; }

    /*
    public int getPilotPoint(Pilot pilot){
        int index = 0;
        for(String row : rank){
            if(pilot.getName().equals(row)){
                return points.get(index);
            }
            index++;
        }
        return -1;
    }
     */
    
    public void setRank(String[] rank){
        int[] table = {35,30,27,25,23,21,19,17,15,13,12,11,10,9,8};
        this.rank = new HashMap< String, Integer>();
        int index = 0;
        for(String row : rank) {
            if (index < table.length) { this.rank.put(row,table[index]); }
            else { this.rank.put(row,0); }
            index++;
        } //for
        Log.e("FERNANDO", "setRank(): " + this.rank);
    } //method
}
