package com.sabir.meusflis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Adapters.CastAdapter;
import com.sabir.meusflis.Adapters.EpisodeAdapter;
import com.sabir.meusflis.Adapters.GenreAdapter;
import com.sabir.meusflis.Models.CastModel;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference seriesRef, userRef;

    private List<String> genreList;
    private List<CastModel> castModels;
    private List<EpisodeModel> episodeModels;

    private GenreAdapter genreAdapter;
    private CastAdapter castAdapter;
    private EpisodeAdapter episodeAdapter;

    private RecyclerView rvGenre, rvEpisodes, rvCast;
    private ImageView ivCover, ivBackground;
    private TextView tvSynopsis;
    private FloatingActionButton fabTrailer;
    private ImageButton btnLike;

    private String userId;
    private String idData, titleData, synopsisData, coverData, backgroundData, trailerData;

    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();

        receiveDataFromIntent();

        setUpUI();

        setupRecyclerView();

        setupLikeButton();

        playTrailer();
    }

    private void initViews() {
        rvGenre = findViewById(R.id.rvGenre);
        rvEpisodes = findViewById(R.id.rvEpisodes);
        rvCast = findViewById(R.id.rvCast);

        ivCover = findViewById(R.id.ivCover);
        ivBackground = findViewById(R.id.ivBackground);

        tvSynopsis = findViewById(R.id.tvSynopsis);

        btnLike = findViewById(R.id.btnLike);

        fabTrailer = findViewById(R.id.fabTrailer);
    }

    private void receiveDataFromIntent() {
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        titleData = intent.getStringExtra("title");
        synopsisData = intent.getStringExtra("synopsis");
        coverData = intent.getStringExtra("cover");
        backgroundData = intent.getStringExtra("background");
        trailerData = intent.getStringExtra("trailer");
        idData = intent.getStringExtra("id");

        castModels = new ArrayList<>();
        Bundle castBundle = intent.getBundleExtra("cast");
        if (castBundle != null) {
            int castSize = castBundle.size() / 2;
            for (int i = 0; i < castSize; i++) {
                String name = castBundle.getString("c_name" + i);
                String url = castBundle.getString("c_url" + i);
                castModels.add(new CastModel(name, url));
            }
        }

        episodeModels = new ArrayList<>();
        Bundle episodesBundle = intent.getBundleExtra("episode");
        if (episodesBundle != null) {
            int episodeCount = episodesBundle.getInt("episode_count", 0);
            for (int i = 0; i < episodeCount; i++) {
                String episodeId = episodesBundle.getString("episode_id" + i);
                String episodeTitle = episodesBundle.getString("episode_title" + i);
                String episodeUrl = episodesBundle.getString("episode_url" + i);
                episodeModels.add(new EpisodeModel(episodeId, episodeTitle, episodeUrl));
            }
        }
    }

    private void setUpUI() {
        Toolbar tbTitle = findViewById(R.id.tbTitle);
        setSupportActionBar(tbTitle);
        getSupportActionBar().setTitle(titleData);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(coverData).into(ivCover);
        Glide.with(this).load(backgroundData).into(ivBackground);
        ivCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        ivBackground.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

        tvSynopsis.setText(synopsisData);

        setupGenreRecyclerView();
    }

    private void setupGenreRecyclerView() {
        genreList = new ArrayList<>();
        Bundle genreBundle = getIntent().getBundleExtra("genre");
        if (genreBundle != null) {
            for (String key : genreBundle.keySet()) {
                genreList.add(genreBundle.getString(key));
            }
        }

        genreAdapter = new GenreAdapter(this, genreList);
        rvGenre.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGenre.setAdapter(genreAdapter);
    }

    private void setupRecyclerView() {
        castAdapter = new CastAdapter(castModels);
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCast.setAdapter(castAdapter);

        episodeAdapter = new EpisodeAdapter(this, episodeModels);
        rvEpisodes.setLayoutManager(new LinearLayoutManager(this));
        rvEpisodes.setAdapter(episodeAdapter);
    }

    private void setupLikeButton() {
        seriesRef = database.getReference("series").child(idData);
        userRef = database.getReference("user").child(userId);

        userRef.child("like_series").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(idData)) {
                    isLiked = true;
                    btnLike.setImageResource(R.drawable.baseline_like_24);
                } else {
                    isLiked = false;
                    btnLike.setImageResource(R.drawable.baseline_unlike_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsActivity.this, "Error al cargar el estado de 'Me gusta'", Toast.LENGTH_SHORT).show();
            }
        });

        btnLike.setOnClickListener(v -> {
            if (isLiked) {
                unlikeSeries();
            }
            else {
                likeSeries();
            }
        });
    }

    private void likeSeries() {
        isLiked = true;
        btnLike.setImageResource(R.drawable.baseline_like_24);
        seriesRef.child("like_counter").setValue(ServerValue.increment(1));
        userRef.child("like_series").child(idData).setValue(idData);
    }

    private void unlikeSeries() {
        isLiked = false;
        btnLike.setImageResource(R.drawable.baseline_unlike_24);
        seriesRef.child("like_counter").setValue(ServerValue.increment(-1));
        userRef.child("like_series").child(idData).removeValue();
    }

    private void playTrailer() {
        fabTrailer.setOnClickListener(v -> {
            if (trailerData != null && !trailerData.isEmpty()) {
                Intent intent = new Intent(DetailsActivity.this, PlayerActivity.class);
                intent.putExtra("vid", trailerData);
                startActivity(intent);
            }
            else {
                Toast.makeText(DetailsActivity.this, "Trailer no disponible", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
