package com.example.meusflis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Catalogue extends MenuActivity {
    ViewFlipper vfCarousel;
    List<ListElement> elements;
    String email;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        // Recuperar los datos del usuario del Intent
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
        }

        vfCarousel = findViewById(R.id.vfCarousel);

        int images[] = {R.drawable.frieren, R.drawable.the_apothecary_diaries, R.drawable.my_favourite_idol};

        for(int image: images){
            flipperImages(image);
        }

        db = new DataBase(this);
        init();
    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        vfCarousel.addView(imageView);
        vfCarousel.setFlipInterval(3000);
        vfCarousel.setAutoStart(true);
        vfCarousel.startFlipping();
    }

    public void init(){
        elements = db.getContents();

        ListAdapter listAdapter = new ListAdapter(elements, this);
        RecyclerView recyclerView = findViewById(R.id.rvCatalogue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.option_profile:
                intent = new Intent(Catalogue.this, Profile.class);
                intent.putExtra("email", email);
                startActivity(intent);
                return true;

            case R.id.option_by_author:

                return true;

            case R.id.option_by_demography:

                return true;

            case R.id.option_by_genre:

                return true;

            case R.id.option_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
                builder.setTitle("Exit App");
                builder.setMessage("¿Do you want to exit the app or log out??");
                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                builder.setNegativeButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Catalogue.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
