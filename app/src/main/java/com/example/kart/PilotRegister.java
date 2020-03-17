package com.example.kart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class PilotRegister extends AppCompatActivity {

    private DrawerLayout drawer;
    DatabaseReference databaseReference;
    ListView listView;
    TextView textView;
    FloatingActionButton floatingActionButton;
    ArrayList<String> listview_pilots = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_register);

        setActionBarTitle(getString(R.string.menu_pilots));

        textView = (TextView) findViewById(R.id.textview_pilot);
        listView = (ListView) findViewById(R.id.listview_pilot);
        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("Pilot");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listview_pilots);
        listView.setAdapter(adapter);

        listviewFuntion();

        fabButton();

    } //onCreate method

    public void listviewFuntion(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listviewAdd(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listviewDel(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listviewItemClick();

    }

    public void listviewItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, false);
                final String key = ids.get(position);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        databaseReference.child(key).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), getString(R.string.msg_pilot_removed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        listview_pilots.add(row);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }

    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        listview_pilots.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }

    public void fabButton(){
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_pilot_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textView.getText().toString();
                textView.setText("");
                if (string.isEmpty())
                    Toast.makeText(v.getContext(), getString(R.string.msg_empty_field),Toast.LENGTH_SHORT).show();
                else
                    registerPilot(string);

            } //fab on click
        }); //fab click listener
    }

    public void registerPilot(String string){
        final Pilot pilot = new Pilot(string);
        Query query = databaseReference.orderByChild("name").equalTo(string);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_pilot_exist), Toast.LENGTH_SHORT).show();
                }
                else {
                    pilot.setId(databaseReference.push().getKey());
                    databaseReference.child(pilot.getId()).setValue(pilot);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_pilot_added), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

} //PilotRegister class
