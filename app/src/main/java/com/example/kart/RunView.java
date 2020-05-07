package com.example.kart;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RunView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_KEY = "key";

    // TODO: Rename and change types of parameters
    private String mKey;

    private View view;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference dbRun;
    private ListView listView;
    private ArrayList<String> ids;
    private ArrayList<String> runs;
    private ArrayAdapter<String> adapter;
    private Run run;

    public RunView() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RunView newInstance(String key) {
        RunView fragment = new RunView();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKey = getArguments().getString(ARG_KEY);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run_view, container, false);

        dbRun = FirebaseDatabase.getInstance().getReference(getString(R.string.db_run));

        runs = new ArrayList<>();
        ids = new ArrayList<>();
        listView = view.findViewById(R.id.listview_run_view);
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.rank_listview_layout, runs);
        listView.setAdapter(adapter);

        run = new Run();

        getRankFromDB();

        fabButton();

        return view;
    }

    public void getRankFromDB(){
        dbRun.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                run = catRank(dataSnapshot, mKey);
                listviewAdd(run);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void listviewAdd(Run run) {
        if (run != null) {
            final Map<String, Integer> sortedRun = sortByValue(run.getRank());
            for (Map.Entry<String, Integer> row: sortedRun.entrySet()) {
                runs.add(row.toString());
                ids.add(mKey);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Map<String, Integer> sortByValue(final Map<String, Integer> wordCounts) {

        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    public void fabButton() {
        floatingActionButton = view.findViewById(R.id.fab_run_view_back);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                RunFragment fragment = new RunFragment();
                openFragment(fragment);
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

    public void openFragment(Object fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_right)
                .replace(R.id.fragment_container, (Fragment) fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
