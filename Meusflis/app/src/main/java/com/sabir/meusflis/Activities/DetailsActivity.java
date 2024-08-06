package com.sabir.meusflis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Adapters.CastAdapter;
import com.sabir.meusflis.Adapters.EpisodeAdapter;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.Models.CastModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private List<CastModel> castModels;

    private List<EpisodeModel> episodeModels;

    private CastAdapter castAdapter;

    private EpisodeAdapter episodeAdapter;

    private RecyclerView rvEpisodes, rvCast;

    private ImageView ivCover, ivBackground;

    private TextView tvTitle, tvSynopsis;

    private FloatingActionButton fabTrailer;

    private String titleData, synopsisData, coverData, episodesData, backgroundData, castData, trailerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        rvEpisodes = findViewById(R.id.rvEpisodes);
        rvCast = findViewById(R.id.rvCast);

        ivCover = findViewById(R.id.ivCover);
        ivBackground = findViewById(R.id.ivBackground);
        tvTitle = findViewById(R.id.tvTitle);
        tvSynopsis = findViewById(R.id.tvSynopsis);
        fabTrailer = findViewById(R.id.fabTrailer);

        titleData = getIntent().getStringExtra("title");
        synopsisData = getIntent().getStringExtra("synopsis");
        coverData = getIntent().getStringExtra("cover");
        episodesData = getIntent().getStringExtra("episodes");
        backgroundData = getIntent().getStringExtra("background");
        castData = getIntent().getStringExtra("cast");
        trailerData = getIntent().getStringExtra("trailer");

        Toolbar toolbar = findViewById(R.id.tbAppName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titleData);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(coverData).into(ivCover);
        Glide.with(this).load(backgroundData).into(ivBackground);
        ivCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        ivBackground.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

        tvTitle.setText(titleData);
        tvSynopsis.setText(synopsisData);

        fabTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference();
                myRef.child("link").child(trailerData).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String vidUrl = snapshot.getValue(String.class);
                        Intent intent = new Intent(DetailsActivity.this , PlayerActivity.class);
                        intent.putExtra("vid", vidUrl);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadCast();
        loadEpisodes();
    }

    private void loadCast() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference castRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCast.setLayoutManager(layoutManager);

        castModels = new ArrayList<>();
        castAdapter = new CastAdapter(castModels);
        rvCast.setAdapter(castAdapter);

        castRef.child("cast").child(castData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot content : snapshot.getChildren()){
                    CastModel castModel = content.getValue(CastModel.class);
                    castModels.add(castModel);
                }
                castAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadEpisodes() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference episodeRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvEpisodes.setLayoutManager(layoutManager);

        episodeModels = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(episodeModels);
        rvEpisodes.setAdapter(episodeAdapter);

        episodeRef.child("link").child(episodesData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot content : snapshot.getChildren()){
                    EpisodeModel episodeModel = content.getValue(EpisodeModel.class);
                    episodeModels.add(episodeModel);
                }
                episodeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}