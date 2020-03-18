package com.example.kart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterRunFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    DatePicker datePicker;
    String date;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_run, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("run");
        datePicker = (DatePicker) view.findViewById(R.id.datepicker_run);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_run_add);

        fabRunAdd();

        return view;
    }

    public void fabRunAdd(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = formatData();
                runRegister();
            }
        });
    }

    public String formatData(){
        int dd,mm,yyyy;
        dd = datePicker.getDayOfMonth();
        if (dd < 10)
            date = "0" + dd;
        else
            date = String.valueOf(dd);
        mm = datePicker.getMonth();
        if (mm < 10)
            date += "0" + mm;
        else
            date += String.valueOf(mm);
        yyyy = datePicker.getYear();
        date += String.valueOf(yyyy);
        return date;
    }

    public void runRegister(){
        final Run run = new Run(date);
        Query query = databaseReference.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getContext(),getString(R.string.msg_run_exist),Toast.LENGTH_SHORT).show();
                }
                else{
                    run.setId(databaseReference.push().getKey());
                    databaseReference.child(run.getId()).setValue(run);
                    Toast.makeText(getContext(),getString(R.string.msg_run_add),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.msg_register));
    }
}
