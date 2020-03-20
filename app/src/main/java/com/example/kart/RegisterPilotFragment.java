package com.example.kart;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterPilotFragment extends Fragment {

    TextView textView;
    ListView listView;
    ArrayList<String> pilots = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;
    DatabaseReference databaseReference;
    FloatingActionButton floatingActionButton;
    View view;

    public RegisterPilotFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_pilot_register));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register_pilot, container, false);

        textView = (TextView) view.findViewById(R.id.textview_pilot);
        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("pilot");

        //listview
        listView = (ListView) view.findViewById(R.id.listview_pilot);
        adapter = new ArrayAdapter<>(container.getContext(),android.R.layout.simple_expandable_list_item_1,pilots);
        listView.setAdapter(adapter);

        listviewFuntion();

        fabButton();

        return view;
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
                        Toast.makeText(getContext(), getString(R.string.msg_pilot_removed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = Objects.requireNonNull(dataSnapshot).getValue(Pilot.class).getName();
        pilots.add(row);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = Objects.requireNonNull(dataSnapshot).getValue(Pilot.class).getName();
        pilots.remove(row);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }

    public void fabButton(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textView.getText().toString().replaceAll("\n"," ").trim();
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
                    Toast.makeText(getContext(), getString(R.string.msg_pilot_exist), Toast.LENGTH_SHORT).show();
                }
                else {
                    pilot.setId(databaseReference.push().getKey());
                    databaseReference.child(pilot.getId()).setValue(pilot);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), getString(R.string.msg_pilot_added), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

} //class
