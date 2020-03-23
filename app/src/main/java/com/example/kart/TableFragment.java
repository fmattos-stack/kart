package com.example.kart;

import android.media.DrmInitData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.Objects;

public class TableFragment extends Fragment {

    private ListView listView;
    private DatabaseReference dbPilot;
    private ArrayList<String> table_list;
    private ArrayList<String> ids;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton floatingActionButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table, container, false);

        dbPilot = FirebaseDatabase.getInstance().getReference("pilot");

        //listview
        table_list = new ArrayList<>();
        ids = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listview_table);
        adapter = new ArrayAdapter<>(container.getContext(),android.R.layout.simple_list_item_1, table_list);
        listView.setAdapter(adapter);
        listviewFunction();

        fabRegister();

        return view;
    }
    //custom methods
    public void fabRegister(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_register);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegisterPilotFragment()).commit();
            }
        });
    } //floating action button
    public void listviewFunction(){
        dbPilot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { listviewAdd(dataSnapshot); }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { listviewDel(dataSnapshot); }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    } //listview actions
    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).rowTable();
        table_list.add(row);
        Collections.sort(table_list);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    } //add function for listview
    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).rowTable();
        table_list.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    } //del function for listview
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_table));
    } //set action titlebar on fragment resume
}
