package com.prajwal.miniinsta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prajwal.miniinsta.Fragments.HomeFragment;
import com.prajwal.miniinsta.Fragments.NotificationFragment;
import com.prajwal.miniinsta.Fragments.ProfileFragment;
import com.prajwal.miniinsta.Fragments.SearchFragment;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.nav_home :
                       selectorFrag = new HomeFragment();
                       break;
                   case R.id.nav_search :
                   selectorFrag = new SearchFragment();
                   break;

                   case R.id.nav_add :
                   selectorFrag = null;
                   startActivity(new Intent(MainActivity.this, PostActivity.class));
                   break;
                   case R.id.nav_fav :
                   selectorFrag = new NotificationFragment();
                   break;
                   case R.id.nav_per :
                   selectorFrag = new ProfileFragment();
                   break;
               }
               if(selectorFrag != null) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, selectorFrag).commit();
               }
               return true;

            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_per);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content , new HomeFragment()).commit();
        }
    }
}