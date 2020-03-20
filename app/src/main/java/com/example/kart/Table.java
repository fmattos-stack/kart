package com.example.kart;

import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Table {

    private ArrayList<Pilot> table;

    public Table(){
        table = new ArrayList<>();
    }

    public void add(Pilot pilot){
        table.add(pilot);
    }

    public void setRank(){
        int position = 0;
        for(Pilot pilot : table){
            table.get(position).setRank(position+1);
            position++;
        }
    }

    public void sortTable(){
        Collections.sort(table, new Comparator<Pilot>() {
            @Override
            public int compare(Pilot o1, Pilot o2) {
                return Double.compare(o1.getTotal_points(), o2.getTotal_points());
            }
        });
    }

    public int getSize(){
        return table.size();
    }

    public boolean setRemove(int position){
        if (isPilotExist(table.get(position).getName())) {
            table.remove(position);
            return true;
        }
        return false;
    }

    public Pilot getPosition(int position){
        return table.get(position);
    }

    public boolean isPilotExist(String name){
        int position = 0;
        for (Pilot pilot : table) {
            if (table.get(position).getName().equals(name)) { return true; }
            position++;
        }
        return false;
    }

    public ArrayList<String> convertToStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        int position = 0;
        for (Pilot pilot : table) {
            String string = table.get(position).getName();
            stringList.add(string);
            position++;
        }
        return stringList;
    }

}
