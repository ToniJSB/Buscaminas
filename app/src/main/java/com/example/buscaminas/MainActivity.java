package com.example.buscaminas;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static SettingsFragment settingsFragment = new SettingsFragment();
    public static StatsFragment statsFragment = new StatsFragment();
    public static GameFragment gameFragment = new GameFragment();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_stop){
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_wait) {
            Toast.makeText(this, "Wait", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();



        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        System.out.println(savedInstanceState);
        // buscar como pasar datos por bundle
        if(savedInstanceState==null){
            Fragment fragment = settingsFragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            getSupportActionBar().setTitle("Buscaminas");

            transaction.commit();
        }
        bottomNavigationView.setOnItemSelectedListener(item ->{
            Fragment selectedFragment = null;
            int variableaEqul = item.getItemId();
            if (R.id.nav_settings == variableaEqul){
                selectedFragment = settingsFragment;
                getSupportActionBar().setTitle("Settings");
            }
            else if (R.id.nav_game == variableaEqul){
                selectedFragment = gameFragment;
                getSupportActionBar().setTitle("Buscaminas");

            }
            else if (R.id.nav_stats == variableaEqul){
                selectedFragment = statsFragment;
                getSupportActionBar().setTitle("Stats");

            }

            if (selectedFragment != null){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();
            }
            return true;
        });

    }


}