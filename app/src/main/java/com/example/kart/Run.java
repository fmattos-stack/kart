package com.example.kart;

import java.util.ArrayList;
import java.util.Date;

public class Run {

    private String id;
    private String date;
    private ArrayList<String> rank;

    public Run(){
    }

    public Run(String date){
        this.date = date;
        //this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getRank() {
        return rank;
    }

    public void setRank(ArrayList<String> rank) {
        this.rank = rank;
    }

    @Override
    public String toString(){
        return date;
    }

}
