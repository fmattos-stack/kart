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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TablesFragment extends Fragment {

    ListView listView;
    DatabaseReference databaseReference;
    ArrayList<String> listview_pilots = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        listView = (ListView) view.findViewById(R.id.listview_table);
        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("Pilot");

        adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,listview_pilots);
        listView.setAdapter(adapter);

        //listviewFunction();

        return view;
    }

    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_table));
    }
}
