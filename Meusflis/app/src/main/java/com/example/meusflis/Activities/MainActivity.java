package com.example.meusflis.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.meusflis.Fragments.DescargasFragment;
import com.example.meusflis.Fragments.HomeFragment;
import com.example.meusflis.Fragments.NuevoPopularFragment;
import com.example.meusflis.Fragments.PerfilFragment;
import com.example.meusflis.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.BLACK);
        }

        setContentView(R.layout.activity_main);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Fragment homeFragment = new HomeFragment();
        Fragment nuevopopularFragment = new NuevoPopularFragment();
        Fragment descargasFragment = new DescargasFragment();
        Fragment perfilFragment = new PerfilFragment();

        if (savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            FragmentManager fragmentManager = getSupportFragmentManager();

            if (item.getItemId() == R.id.nav_home){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
            }
            else if (item.getItemId() == R.id.nav_nuevopopular){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, nuevopopularFragment).commit();
            }
            else if (item.getItemId() == R.id.nav_descargas){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, descargasFragment).commit();
            }
            else if (item.getItemId() == R.id.nav_perfil){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, perfilFragment).commit();
            }

            return true;
        });
    }
}