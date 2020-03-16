package com.example.kart;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PilotFragment extends Fragment {

    private FragmentActivity listener;
    private String string;
    private Table table = new Table();
    private ArrayAdapter arrayAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pilots, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //textview object - user input
        final TextView textView = (TextView)view.findViewById(R.id.input_pilot);

        //listview object
        final ListView listView = (ListView) view.findViewById(R.id.listview);
        //skip arrayAdapter instance if table isnt instanced
        if (!table.isNull())
            initArrayAdapter(listView);

        //item selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mensage = String.format("%s: %s", getString(R.string.msg_pilot_removed), table.getPosition(position).getName());
                if (table.setRemove(position)) {
                    initArrayAdapter(listView);
                    arrayAdapter.notifyDataSetChanged();
                }
                else mensage = getString(R.string.msg_pilot_not_removed) + ": " + table.getPosition(position).getName();
                Toast.makeText(getActivity(), mensage,Toast.LENGTH_SHORT).show();
            }
        });

        //Floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_pilot_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensage;
                string = textView.getText().toString();
                textView.setText("");
                if (string.isEmpty()){ mensage = getString(R.string.msg_empty_field); }
                else {
                    Pilot pilot = new Pilot(string);
                    if (table.setAdd(pilot)) {
                        initArrayAdapter(listView);
                        arrayAdapter.notifyDataSetChanged();
                        mensage = getString(R.string.msg_pilot_added) + ": " + string;
                    }
                    else
                        mensage = getString(R.string.msg_pilot_exist);
                }
                Toast.makeText(getActivity(), mensage,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initArrayAdapter(ListView listView){
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, table.convertToStringList());
        listView.setAdapter(arrayAdapter);
    }
}