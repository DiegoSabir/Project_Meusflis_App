package com.sabir.meusflis.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.sabir.meusflis.Fragments.HomeFragment;
import com.sabir.meusflis.Fragments.ProfileFragment;
import com.sabir.meusflis.Fragments.SearchFragment;
import com.sabir.meusflis.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnvMenu;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tbAppName = findViewById(R.id.tbTitle);
        setSupportActionBar(tbAppName);
        getSupportActionBar().setTitle("Meusflis");

        FirebaseApp.initializeApp(this);

        bnvMenu = findViewById(R.id.bnvMenu);

        userId = getIntent().getStringExtra("userId");

        Fragment homeFragment = new HomeFragment();
        Fragment searchFragment = new SearchFragment();
        Fragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        homeFragment.setArguments(bundle);

        if (savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
        }

        bnvMenu.setOnNavigationItemSelectedListener(item -> {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (item.getItemId() == R.id.nav_home){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                homeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
            }
            else if (item.getItemId() == R.id.nav_search){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                searchFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, searchFragment).commit();
            }
            else if (item.getItemId() == R.id.nav_profile){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment).commit();
            }
            return true;
        });
    }
}