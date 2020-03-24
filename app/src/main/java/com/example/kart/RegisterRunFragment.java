package com.example.kart;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private TextView textView;
    private TextView textView_index;
    private ArrayList<String> rank;
    private DatabaseReference dbRun;
    private DatabaseReference dbPilot;
    private Run run;
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> list;
    private ArrayList<String> keys;
    private ArrayList<String> selectedRow;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_run, container, false);

        dbRun = FirebaseDatabase.getInstance().getReference(getString(R.string.db_run));
        dbPilot = FirebaseDatabase.getInstance().getReference(getString(R.string.db_pilot));
        datePicker = view.findViewById(R.id.datepicker_run);
        textView = view.findViewById(R.id.textview_run);
        textView_index = view.findViewById(R.id.textview_index);
        floatingActionButton = view.findViewById(R.id.fab_run_add);
        selectedRow = new ArrayList<>();
        rank = new ArrayList<>();

        //listview
        list = new ArrayList<>();
        keys = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_multiple_choice,list);
        listView = view.findViewById(R.id.listview_registerRun);
        listView.setAdapter(adapter);

        listviewFuntion();

        fabRunAdd();

        return view;
    }
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
                String row = listView.getItemAtPosition(position).toString();
                if (selectedRow.contains(row)) { selectedRow.remove(row); }
                else { selectedRow.add(row); }
                row = "";
                String index_row = "";
                int index = 1;
                for (String lineAdd : selectedRow){
                    row += lineAdd + "\n";
                    textView.setText(row);
                    index_row += index + "\n";
                    textView_index.setText(index_row);
                    index++;
                }
            }
        });
    } //listview item click action
    public void listviewAdd(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        list.add(row);
        String key = dataSnapshot.getKey();
        keys.add(key);
        adapter.notifyDataSetChanged();
    } //add function for listview
    public void listviewDel(@NonNull DataSnapshot dataSnapshot){
        String row = dataSnapshot.getValue(Pilot.class).getName();
        list.remove(row);
        String key = dataSnapshot.getKey();
        keys.remove(key);
        adapter.notifyDataSetChanged();
    } //del function for listview
    //custom methods
    public void fabRunAdd(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().isEmpty())
                    Toast.makeText(getContext(), R.string.msg_empty_field, Toast.LENGTH_SHORT).show();
                else {
                    formatRank();
                    formatDate();
                    runRegister();
                }
            }
        });
    } //floating action button add new run
    public void formatRank(){
        String[] inputPilots = textView.getText().toString().trim().split("\n");
        textView.setText("");
        textView_index.setText("");
        for(String row : inputPilots){ rank.add(row); }
    } //get input into array, remove spaces, check duplicates and calls capitalize function
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Pilot> pilots = getPilots(dataSnapshot); //get pilots from db
                if (pilots != null) {
                    for (String rowRank : rank) {
                        for (Pilot pilot : pilots) {
                            if (rowRank.equals(pilot.getName())) {
                                pilot.setTotal_points(run.catPilotPoint(pilot));
                                pilot.setRuns(1);
                            } //if rank line is in pilot line
                        } //for pilots objects
                    } //for rank lines
                    pilotsSort(pilots);
                    for (Pilot pilot : pilots){ dbPilot.child(pilot.getId()).setValue(pilot); }
                } //if pilots not null
            } //onDataChange
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    } //calculate total points
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void pilotsSort(@NotNull ArrayList<Pilot> pilots){
        int position = 1;
        pilots.sort(Collections.<Pilot>reverseOrder());
        for(Pilot pilot:pilots) {
            pilot.setRank(position);
            Log.d("Fernando: ", String.format("%d %s %d",pilot.getRank(),pilot.getName(),pilot.getTotal_points()));
            position++;
        }
    } //reverse sort and rank index update
    public ArrayList<Pilot> getPilots(@NotNull DataSnapshot ds){
        ArrayList<Pilot> pilots = new ArrayList<>();
        for(DataSnapshot rowDS : ds.getChildren()){
            Pilot pilot = rowDS.getValue(Pilot.class);
            pilots.add(pilot);
        }
        return pilots;
    } //getPilots from db
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume(){
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.msg_register));
    } //set action titlebar on fragment
}
