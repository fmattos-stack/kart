package com.example.kart;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference databaseReference;

    public DatabaseReference getDatabaseReference(){
        databaseReference = FirebaseDatabase.getInstance().getReference("database");
        return databaseReference;
    }

}
