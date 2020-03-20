package com.example.kart;

import java.util.ArrayList;

public class Run {

    private String id;
    private String date;
    private ArrayList<String> rank;
    private ArrayList<Integer> points;

    public Run(){
        this.rank = new ArrayList<>();
        this.points = new ArrayList<>();
    }

    public Run(String date, ArrayList<String> rank){
        this.date = date;
        this.rank = rank;
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

    public void setPoint(){
        int position = 1;
        for(String row : rank){
            switch (position){
                case 1 : points.add(35);
                    break;
                case 2 : points.add(30);
                    break;
                case 3 : points.add(27);
                    break;
                case 4 : points.add(25);
                break;
                case 5 : points.add(23);
                break;
                case 6 : points.add(21);
                break;
                case 7 : points.add(19);
                break;
                case 8 : points.add(17);
                break;
                case 9 : points.add(15);
                break;
                case 10 : points.add(13);
                break;
                case 11 : points.add(12);
                break;
                case 12 : points.add(11);
                break;
                case 13 : points.add(10);
                break;
                case 14 : points.add(9);
                break;
                case 15 : points.add(8);
                break;
                default : points.add(0);
                break;
            } //switch
            position++;
        } //for
    } //method
}
