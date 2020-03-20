package com.example.kart;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterRunFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    DatePicker datePicker;
    String date;
    EditText editText;
    ArrayList<String> rank = new ArrayList<>();
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_run, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("database").child("run");
        datePicker = (DatePicker) view.findViewById(R.id.datepicker_run);
        editText = (EditText) view.findViewById(R.id.textview_run);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_run_add);

        fabRunAdd();

        return view;
    }

    public void fabRunAdd(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatData();
                formatRank();
                runRegister();
            }
        });
    }

    public void formatRank(){
        String[] array = editText.getText().toString().split("\n");
        for(String position : array)
            rank.add(position);
    }

    public void formatData(){
        int dd,mm,yyyy;
        dd = datePicker.getDayOfMonth();
        if (dd < 10)
            date = "0" + dd;
        else
            date = String.valueOf(dd);
        mm = datePicker.getMonth() + 1;
        if (mm < 10)
            date += "0" + mm;
        else
            date += String.valueOf(mm);
        yyyy = datePicker.getYear();
        date += String.valueOf(yyyy);
    }

    public void runRegister(){
        final Run run = new Run(date,rank);
        Query query = databaseReference.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getContext(),getString(R.string.msg_run_exist) + date,Toast.LENGTH_SHORT).show();
                }
                else{
                    run.setId(databaseReference.push().getKey());
                    run.setRank(rank);
                    databaseReference.child(run.getId()).setValue(run);
                    Toast.makeText(getContext(),getString(R.string.msg_run_add),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.msg_register));
    }
}
