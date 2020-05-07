package com.example.kart;

import android.media.MediaCas;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RunFragment extends Fragment {

    private View view;
    private TextView textView;
    private ListView listView;
    private DatabaseReference dbRun;
    private DatabaseReference dbPilot;
    private ArrayList<String> runs;
    private ArrayList<String> ids;
    private ArrayList<String> currentRank = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run, container, false);

        textView = view.findViewById(R.id.textview_run);
        dbRun = FirebaseDatabase.getInstance().getReference(getString(R.string.db_run));
        dbPilot = FirebaseDatabase.getInstance().getReference(getString(R.string.db_pilot));

        //listview
        runs = new ArrayList<>();
        ids = new ArrayList<>();
        listView = view.findViewById(R.id.listview_run);
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, runs);
        listView.setAdapter(adapter);

        listviewFunction();

        fabButton();

        return view;
    }

    public void listviewFunction() {
        dbRun.addChildEventListener(new ChildEventListener() {
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

                final String key = ids.get(position);
                RunView fragment = RunView.newInstance(key);
                openFragment(fragment);

                /*
                dbRun.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        /*
                        removepoints(dataSnapshot, key);
                        dbRun.child(key).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getString(R.string.msg_run_del), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                 */
            }
        });
    }

    public void removepoints(final DataSnapshot dsRun, final String key) {
        dbPilot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Pilot> pilots = getPilots(dataSnapshot);
                Run run = catRank(dsRun, key);
                for (Pilot pilotRow : pilots) {
                    pilotRow.setTotal_points(run.catPilotPoint(pilotRow) * -1);
                    pilotRow.setRuns(-1);
                    dbPilot.child(pilotRow.getId()).setValue(pilotRow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Run catRank(@NotNull DataSnapshot ds, String key) {
        for (DataSnapshot rowRun : ds.getChildren()) {
            Run run = rowRun.getValue(Run.class);
            if (run.getId().equals(key)) {
                return run;
            }
        }
        return null;
    }

    public ArrayList<Pilot> getPilots(@NotNull DataSnapshot ds) {
        ArrayList<Pilot> pilots = new ArrayList<>();
        for (DataSnapshot dsRow : ds.getChildren()) {
            pilots.add(dsRow.getValue(Pilot.class));
        }
        return pilots;
    }

    public void listviewAdd(@NonNull DataSnapshot dataSnapshot) {
        String row = dataSnapshot.getValue().toString();
        String formatedRow = formatRow(row);
        runs.add(formatedRow);
        String key = dataSnapshot.getKey();
        ids.add(key);
        adapter.notifyDataSetChanged();
    }

    public String formatRow(@NotNull String row) {
        String[] splited;
        splited = row.substring(1, row.length() - 1).split(",");
        return splited[0];
    }

    public void listviewDel(@NonNull DataSnapshot dataSnapshot) {
        String row = dataSnapshot.getValue().toString();
        String formatedRow = formatRow(row);
        runs.remove(formatedRow);
        String key = dataSnapshot.getKey();
        ids.remove(key);
        adapter.notifyDataSetChanged();
    }

    public void fabButton() {
        floatingActionButton = view.findViewById(R.id.fab_run_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                RegisterRunFragment fragment = new RegisterRunFragment();
                openFragment(fragment);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.menu_run));
    }

    public void openFragment(Object fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, (Fragment) fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}