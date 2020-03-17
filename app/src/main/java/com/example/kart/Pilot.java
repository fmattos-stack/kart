package com.example.kart;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Pilot {

    private String id;
    private String name;
    private int points;
    private int rank;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("database").child("Pilot");
    private DataSnapshot dataSnapshot;

    public Pilot(String name){
        this.setName(name);
        this.setPoints(0);
    }

    public String getId() {
        return this.id;
    }

    private void setId(String id) {
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
        return name;
    }


    public boolean addPilot(Pilot inputPilot){
        Pilot pilot = inputPilot;
        for( DataSnapshot data : dataSnapshot.getChildren()) {
            //check if user exist
            if (data.child("name").exists())
                return false;
        }

        pilot.setId(databaseReference.push().getKey());
        databaseReference.child(pilot.getId()).setValue(pilot);

        //pilots.add(pilot);
        return true;
    }
}



