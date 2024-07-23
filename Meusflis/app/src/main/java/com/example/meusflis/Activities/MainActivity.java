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

import com.example.meusflis.Common.Common;
import com.example.meusflis.Fragments.DescargasFragment;
import com.example.meusflis.Fragments.HomeFragment;
import com.example.meusflis.Fragments.NuevoPopularFragment;
import com.example.meusflis.Fragments.PerfilFragment;
import com.example.meusflis.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FrameLayout container;
    private BottomNavigationView bottomNavigationView;
    public BadgeDrawable badgeDrawable;

    DatabaseReference reference;

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

        reference = FirebaseDatabase.getInstance().getReference();

        Fragment homeFragment = new HomeFragment();
        Fragment nuevopopularFragment = new NuevoPopularFragment();
        Fragment descargasFragment = new DescargasFragment();
        Fragment perfilFragment = new PerfilFragment();

        if (savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
        }

        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.nav_nuevopopular);
        badgeDrawable.setVisible(false);
        badgeDrawable.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        cargarItemsEstrenos();

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

    private void cargarItemsEstrenos() {
        reference.child(Common.NODO_MOVIES)
                .child(Common.NODO_ESTRENOS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int quantity = (int) snapshot.getChildrenCount();

                            if (quantity > 0){
                                badgeDrawable.setVisible(true);
                                badgeDrawable.setNumber(quantity);
                            }
                            else{
                                badgeDrawable.setVisible(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}