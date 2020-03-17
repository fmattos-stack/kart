package com.example.kart;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Table {

    private DatabaseReference databaseReference;
    private ArrayList<Pilot> pilots;

    public Table(){
        pilots = new ArrayList<>();
    }

    public boolean isNull(){
        if (pilots == null) {
            return true;
        }
         return false;
    }

    public void setRank(){
        int position = 0;
        for(Pilot pilot : pilots){
            pilots.get(position).setRank(position+1);
            position++;
        }
    }

    public void sortTable(){
        Collections.sort(pilots, new Comparator<Pilot>() {
            @Override
            public int compare(Pilot o1, Pilot o2) {
                return Double.compare(o1.getPoints(), o2.getPoints());
            }
        });
    }

    public int getSize(){
        return pilots.size();
    }

    public boolean setRemove(int position){
        if (isPilotExist(pilots.get(position).getName())) {
            pilots.remove(position);
            return true;
        }
        return false;
    }

    public Pilot getPosition(int position){
        return pilots.get(position);
    }

    public boolean isPilotExist(String name){
        int position = 0;
        for (Pilot pilot : pilots) {
            if (pilots.get(position).getName().equals(name)) { return true; }
            position++;
        }
        return false;
    }

    public ArrayList<String> convertToStringList() {
        ArrayList<String> stringList = new ArrayList<>();
        int position = 0;
        for (Pilot pilot : pilots) {
            String string = pilots.get(position).getName();
            stringList.add(string);
            position++;
        }
        return stringList;
    }

    public void splitPilotInput(){
        //metodo
    }
}
