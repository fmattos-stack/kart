package com.example.kart;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterRunFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;
    private DatePicker datePicker;
    private String date;
    private EditText editText;
    private ArrayList<String> rank;
    private DatabaseReference dbRun;
    private DatabaseReference dbPilot;
    private Run run;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_run, container, false);

        dbRun = FirebaseDatabase.getInstance().getReference("run");
        dbPilot = FirebaseDatabase.getInstance().getReference("pilot");
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
                if (editText.getText().toString().isEmpty())
                    Toast.makeText(getContext(), R.string.msg_empty_field, Toast.LENGTH_SHORT).show();
                else if(formatRank()){
                    formatDate();
                    runRegister();
                }
                else
                    Toast.makeText(getContext(),R.string.msg_pilot_duplicate, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean formatRank(){
        String[] inputPilots = editText.getText().toString().trim().split("\n");
        boolean isDuplicate = false;
        for(int i = 0; i < inputPilots.length; i++) {
            for (int j = i + 1; j < inputPilots.length; j++) {
                if (j != i && inputPilots[i].equals(inputPilots[j])) {
                    isDuplicate = true;
                }
            }
        }
        if(!isDuplicate) {
            editText.setText("");
            rank = capitalizeRank(inputPilots);
            return true;
        }
        return false;
    } //get input into array, remove spaces, check duplicates and calls capitalize function
    public ArrayList<String> capitalizeRank(@NotNull String[] input){
        ArrayList<String> capitalized = new ArrayList<>();
        for (String rowPilot : input){
            if (!rowPilot.isEmpty()) capitalized.add(rowPilot.substring(0,1).toUpperCase() + rowPilot.substring(1));
        }
        return capitalized;
    } //capitalize every strings
    public void formatDate(){
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
    } //get input date and add a left zero if needed
    public void runRegister(){
        run = new Run();
        Query query = dbRun.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getContext(),getString(R.string.msg_run_exist) + " " + date,Toast.LENGTH_SHORT).show();
                }
                else{
                    run.setId(dbRun.push().getKey());
                    run.setDate(date);
                    run.setRank(rank);
                    dbRun.child(run.getId()).setValue(run);
                    calcPilotsPoints();
                    Toast.makeText(getContext(),getString(R.string.msg_run_add),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    } //check if input exists, write a new record and calls calcPilotsPoints()
    public void calcPilotsPoints(){
        dbPilot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Pilot> pilots;
                pilots = getPilots(dataSnapshot); //get pilots from db
                for (String rowPilotName : rank) {
                    for (Pilot rowPilot : pilots)
                        if (rowPilotName.equals(rowPilot.getName())){
                            rowPilot.setTotal_points(run.catPilotPoint(rowPilot));
                            rowPilot.setRuns(1);
                            rowPilot.sort(pilots);
                            dbPilot.child(rowPilot.getId()).setValue(rowPilot);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public ArrayList<Pilot> getPilots(DataSnapshot ds){
        ArrayList<Pilot> pilots = new ArrayList<>();
        for(DataSnapshot rowDS : ds.getChildren()){
            Pilot pilot = rowDS.getValue(Pilot.class);
            pilots.add(pilot);
        }
        return pilots;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.msg_register));
    }
}
