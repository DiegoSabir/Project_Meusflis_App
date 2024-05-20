package com.example.meusflis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Catalogue extends MenuActivity implements SearchView.OnQueryTextListener {
    private ViewFlipper vfCarousel;
    private SearchView svSearchMultimediaContent;
    private Spinner spGenre, spDemographicCategories;
    private RecyclerView recyclerView;
    private List<MultimediaContent> elements;
    private String email;
    private ListAdapter listAdapter;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        email = getIntent().getStringExtra("email");

        initViews();
        initViewFlipper();
        db = new Database(this);

        initSpinners();
        initListeners();
        populateGenreSpinner();
    }

    private void initViews() {
        vfCarousel = findViewById(R.id.vfCarousel);
        spGenre = findViewById(R.id.spGenre);
        spDemographicCategories = findViewById(R.id.spDemographicCategories);
        svSearchMultimediaContent = findViewById(R.id.svSearchMultimediaContent);
        recyclerView = findViewById(R.id.rvCatalogue);
    }

    private void initViewFlipper() {
        int images[] = {R.drawable.frieren, R.drawable.the_apothecary_diaries, R.drawable.my_favourite_idol};
        for (int image : images) {
            flipperImages(image);
        }
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        vfCarousel.addView(imageView);
        vfCarousel.setFlipInterval(3000);
        vfCarousel.setAutoStart(true);
        vfCarousel.startFlipping();
    }

    private void initSpinners() {
        ArrayAdapter<CharSequence> demographicAdapter = ArrayAdapter.createFromResource(this, R.array.demographic_array, android.R.layout.simple_spinner_item);
        demographicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDemographicCategories.setAdapter(demographicAdapter);
    }

    private void initListeners() {
        spGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                init();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });

        spDemographicCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                init();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No action needed
            }
        });
        svSearchMultimediaContent.setOnQueryTextListener(this);
    }

    private void populateGenreSpinner() {
        List<String> genreNames = db.getGenreNames();
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenre.setAdapter(genreAdapter);
    }

    public void init() {
        String selectedGenre = spGenre.getSelectedItem() != null ? spGenre.getSelectedItem().toString() : "";
        String selectedDemographic = spDemographicCategories.getSelectedItem() != null ? spDemographicCategories.getSelectedItem().toString() : "";

        elements = db.getFilteredContents(selectedGenre, selectedDemographic);
        listAdapter = new ListAdapter(elements, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.searchFilter(newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_profile:
                startProfileActivity();
                return true;

            case R.id.option_spanish:

                return true;

            case R.id.option_galician:

                return true;

            case R.id.option_exit:
                showExitDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startProfileActivity() {
        Intent intent = new Intent(Catalogue.this, Profile.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setTitle(getString(R.string.exit_dialog_title)) // Obtener el título desde strings.xml
                .setMessage(getString(R.string.exit_dialog_message)) // Obtener el mensaje desde strings.xml
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();
                    }
                })
                .show();
    }


    private void logOut() {
        Intent intent = new Intent(Catalogue.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ListAdapter.handleContextMenuSelection(item, Catalogue.this);
        return true;
    }
}
