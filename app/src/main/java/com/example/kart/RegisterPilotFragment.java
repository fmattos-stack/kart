package com.example.kart;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class RegisterPilotFragment extends Fragment {

    private TextView textView;
    private ListView listView;
    private ArrayList<String> pilots;
    private ArrayList<String> ids;
    private ArrayAdapter<String> adapter;
    private DatabaseReference dbPilot;
    private FloatingActionButton floatingActionButton;
    private View view;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_pilot_register));
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register_pilot, container, false);

        textView = view.findViewById(R.id.textview_pilot);
        dbPilot = FirebaseDatabase.getInstance().getReference(getString(R.string.db_pilot));

        //listview
        pilots = new ArrayList<>();
        ids = new ArrayList<>();
        adapter = new ArrayAdapter<>(container.getContext(),R.layout.pilot_listview_layout,pilots);
        listView = view.findViewById(R.id.listview_pilot);
        listView.setAdapter(adapter);

        listviewFuntion();

        fabButton();

        return view;
    } //onCreate method
    public void listviewFuntion(){
        dbPilot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listviewAdd(dataSnapshot);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {  }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                listviewDel(dataSnapshot);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });

        listviewItemClick();

    } //listview actions
    public void listviewItemClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, false);
                final String key = ids.get(position);
                dbPilot.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dbPilot.child(key).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getString(R.string.msg_pilot_removed), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
    } //listview item click action
    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        pilots.add(row);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    } //add function for listview
    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        pilots.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    } //del function for listview
    public void fabButton(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textView.getText().toString().replaceAll("\n"," ").trim();
                textView.setText("");
                if (string.isEmpty())
                    Toast.makeText(v.getContext(), getString(R.string.msg_empty_field),Toast.LENGTH_SHORT).show();
                else {
                    string = capitalizeString(string);
                    registerPilot(string);
                }
            } //fab on click
        }); //fab click listener
    } //floating action button
    public String capitalizeString(@NotNull String string){
        String[] words = string.split(" ");
        String capitalizedWord = "";
        for (String row : words) {
            capitalizedWord += row.substring(0, 1).toUpperCase() + row.substring(1).toLowerCase() + " ";
        }
        return capitalizedWord.trim();
    }
    public void registerPilot(String string){
        final Pilot pilot = new Pilot(string);
        Query query = dbPilot.orderByChild("name").equalTo(string);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getContext(), getString(R.string.msg_pilot_exist), Toast.LENGTH_SHORT).show();
                }
                else {
                    pilot.setId(dbPilot.push().getKey());
                    dbPilot.child(pilot.getId()).setValue(pilot);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), getString(R.string.msg_pilot_added), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    } //register pilot

} //class
