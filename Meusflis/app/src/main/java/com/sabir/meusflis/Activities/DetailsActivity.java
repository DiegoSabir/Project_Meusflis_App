package com.sabir.meusflis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.sabir.meusflis.Models.CastModel;
import com.sabir.meusflis.Adapters.EpisodeAdapter;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<CastModel> castModels;
    private List<EpisodeModel> episodeModels;

    private CastAdapter castAdapter;
    private EpisodeAdapter episodeAdapter;

    private RecyclerView rvEpisodes, rvCast;

    private ImageView ivThumb, ivCover;
    private TextView tvDescription;
    private FloatingActionButton fabTrailer;
    private ImageButton ibLike;

    private String id_data, title_data, description_data, thumb_data, link_data, cover_data, cast_data, trailer_data;

    private String userId;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivThumb = findViewById(R.id.ivThumb);
        ivCover = findViewById(R.id.ivCover);
        tvDescription = findViewById(R.id.tvDescription);
        fabTrailer = findViewById(R.id.fabTrailer);
        ibLike = findViewById(R.id.ibLike);

        rvCast = findViewById(R.id.rvCast);
        rvEpisodes = findViewById(R.id.rvEpisodes);

        id_data = getIntent().getStringExtra("idSeries");
        title_data = getIntent().getStringExtra("title");
        description_data = getIntent().getStringExtra("description");
        thumb_data = getIntent().getStringExtra("thumb");
        link_data = getIntent().getStringExtra("link");
        cover_data = getIntent().getStringExtra("cover");
        cast_data = getIntent().getStringExtra("cast");
        trailer_data = getIntent().getStringExtra("trailer");

        userId = getIntent().getStringExtra("userId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title_data);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String coverUrl = "https://drive.google.com/uc?export=download&id=" + cover_data;
        String thumbUrl = "https://drive.google.com/uc?export=download&id=" + thumb_data;
        Glide.with(this).load(thumbUrl).into(ivThumb);
        Glide.with(this).load(coverUrl).into(ivCover);
        ivThumb.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        ivCover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

        tvDescription.setText(description_data);

        setupLikeButton();
        loadTrailer();
        loadCast();
        loadEpisode();
    }

    private void setupLikeButton() {
        DatabaseReference seriesReference = database.getReference("series").child(id_data);
        DatabaseReference userReference = database.getReference("user").child(userId);

        userReference.child("likeSeries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(id_data)) {
                    isLiked = true;
                    ibLike.setImageResource(R.drawable.like_on);
                } else {
                    isLiked = false;
                    ibLike.setImageResource(R.drawable.like_off);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsActivity.this, "Error al cargar el estado de 'Me gusta'", Toast.LENGTH_SHORT).show();
            }
        });

        ibLike.setOnClickListener(v -> {
            if (isLiked) {
                isLiked = false;
                ibLike.setImageResource(R.drawable.like_off);
                seriesReference.child("like").setValue(ServerValue.increment(-1));
                userReference.child("likeSeries").child(id_data).removeValue();
            }
            else {
                isLiked = true;
                ibLike.setImageResource(R.drawable.like_on);
                seriesReference.child("like").setValue(ServerValue.increment(1));
                userReference.child("likeSeries").child(id_data).setValue(id_data);
            }
        });
    }

    private void loadTrailer() {
        fabTrailer.setOnClickListener(v -> {
            if (trailer_data != null && !trailer_data.isEmpty()) {
                Intent intent = new Intent(DetailsActivity.this, PlayerActivity.class);
                intent.putExtra("vid", trailer_data); // Usamos trailer_data directamente
                intent.putExtra("title", title_data);
                startActivity(intent);
            } else {
                Toast.makeText(DetailsActivity.this, "Trailer no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadEpisode() {
        DatabaseReference episodeReference = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvEpisodes.setLayoutManager(layoutManager);

        episodeModels = new ArrayList<>();
        episodeAdapter = new EpisodeAdapter(episodeModels);
        rvEpisodes.setAdapter(episodeAdapter);

        episodeReference.child("episode").child(link_data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot content:snapshot.getChildren()){
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

    private void loadCast() {
        DatabaseReference castReference = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCast.setLayoutManager(layoutManager);

        castModels = new ArrayList<>();
        castAdapter = new CastAdapter(castModels);
        rvCast.setAdapter(castAdapter);

        castReference.child("cast").child(cast_data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot content:snapshot.getChildren()){
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}