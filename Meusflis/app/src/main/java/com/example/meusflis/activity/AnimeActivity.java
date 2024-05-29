package com.example.meusflis.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meusflis.R;
import com.example.meusflis.adapter.EpisodeAdapter;
import com.example.meusflis.database.DatabaseHelper;
import com.example.meusflis.model.Episode;

import java.util.ArrayList;

public class AnimeActivity extends AppCompatActivity {

    ImageView ivStatusAnime;
    TextView tvStatusAnime;
    Spinner spSeason;
    ListView lvEpisode;
    ArrayList<Episode> listEpisode;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        initializeViews();
    }

    

    private void initializeViews() {
        ivStatusAnime = findViewById(R.id.ivStatusAnime);
        tvStatusAnime = findViewById(R.id.tvStatusAnime);
        spSeason = findViewById(R.id.spSeason);
        lvEpisode = findViewById(R.id.lvEpisode);
    }



    private void setupEpisodeList() {
        listEpisode = databaseHelper.getEpisodeData();
        EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, listEpisode);
        lvEpisode.setAdapter(episodeAdapter);
    }

}
