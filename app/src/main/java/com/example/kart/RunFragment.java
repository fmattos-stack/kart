package com.example.kart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class RunFragment extends Fragment {

    View view;
    TextView textView_rank;
    ListView listView_run;
    DatabaseReference databaseReference;
    ArrayList<String> runs = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run, container, false);

        textView_rank = (TextView) view.findViewById(R.id.textview_run_rank);
        listView_run = (ListView) view.findViewById(R.id.listview_run);
        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("Run");

        fabButton();

        return view;
    }

    public void fabButton(){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_run_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RegisterRunFragment()).commit();
            }
        });
    }

    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_run));
    }
}
