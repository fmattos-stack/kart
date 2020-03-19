package com.example.kart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.EventListener;

public class TableFragment extends Fragment {

    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<String> table = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FloatingActionButton floatingActionButton;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("pilot");

        //listview
        listView = (ListView) view.findViewById(R.id.listview_table);
        adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1, table);
        listView.setAdapter(adapter);

        listviewFunction();

        fabRegister();

        return view;
    }

    public void fabRegister(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_register);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegisterPilotFragment()).commit();
            }
        });
    }

    public void listviewFunction(){
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
    }

    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).toString();
        table.add(row);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }

    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).toString();
        table.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }

    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_table));
    }
}
