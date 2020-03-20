package com.example.kart;

import android.os.Build;
import android.os.Bundle;
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
import java.util.EventListener;
import java.util.Objects;

public class TableFragment extends Fragment {

    private ListView listView;
    private DatabaseReference dbPilot;
    private DatabaseReference dbRun;
    private ArrayList<String> table_list = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FloatingActionButton floatingActionButton;
    private View view;
    private Pilot pilot = new Pilot();
    private Run run;
    private ArrayList<Run> runs = new ArrayList<>();
    private Table table = new Table();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table, container, false);

        dbPilot = FirebaseDatabase.getInstance().getReference("pilot");
        dbRun = FirebaseDatabase.getInstance().getReference("run");

        //listview
        listView = (ListView) view.findViewById(R.id.listview_table);
        adapter = new ArrayAdapter<>(container.getContext(),android.R.layout.simple_list_item_1, table_list);
        listView.setAdapter(adapter);

        listviewFunction();

        fabRegister();

        getPilots();
        getRuns();
        //calcTable();

        return view;
    }

    public void calcTable(){
        for(Run run : runs){

        }
    }

    public void getPilots() {
        dbPilot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pilot = dataSnapshot.getValue(Pilot.class);
                table.add(pilot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getRuns(){
        dbRun.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                run = dataSnapshot.getValue(Run.class);
                runs.add(run);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void fabRegister(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_register);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RegisterPilotFragment()).commit();
            }
        });
    }

    public void listviewFunction(){
        dbPilot.addChildEventListener(new ChildEventListener() {

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
        table_list.add(row);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }

    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).toString();
        table_list.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_table));
    }
}
