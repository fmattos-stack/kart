 package com.example.kart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


 public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

     private DrawerLayout drawer;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PilotFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_pilots);
        }
    }

     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_table:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TablesFragment()).commit();
                break;
            case R.id.nav_runs:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RunsFragment()).commit();
                break;
            case R.id.nav_pilots:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PilotFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
         return true;
     }

     @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

     public void setActionBarTitle(String title) {
         getSupportActionBar().setTitle(title);
     }


 }
