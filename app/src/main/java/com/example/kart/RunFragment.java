package com.example.kart;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class RunFragment extends Fragment {

    private View view;
    private TextView textView;
    private ListView listView;
    private DatabaseReference dbRun;
    private ArrayList<String> runs = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run, container, false);

        textView = view.findViewById(R.id.textview_run);
        dbRun = FirebaseDatabase.getInstance().getReference(getString(R.string.db_run));

        //listview
        listView = view.findViewById(R.id.listview_run);
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_list_item_1,runs);
        listView.setAdapter(adapter);

        listviewFunction();

        fabButton();

        return view;
    }

    public void listviewFunction(){
        dbRun.addChildEventListener(new ChildEventListener() {
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

        listviewItemClick();
    }
    public void listviewItemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String key = ids.get(position);
                dbRun.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dbRun.child(key).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getString(R.string.msg_run_del), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
    }
    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue().toString();
        String formatedRow = formatRow(row);
        runs.add(formatedRow);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }
    public String formatRow(String row){
        String [] splited;
        splited = row.substring(1,row.length()-1).split(",");
        return splited[0];
    }
    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue().toString();
        String formatedRow = formatRow(row);
        runs.remove(formatedRow);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }
    public void fabButton(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_run_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RegisterRunFragment()).commit();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_run));
    }
}
