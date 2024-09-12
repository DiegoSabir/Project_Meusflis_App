package com.sabir.meusflis.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sabir.meusflis.Activities.DetailsActivity;
import com.sabir.meusflis.Models.CastModel;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private final Context context;
    private List<SeriesModel> seriesList;
    private final String userId;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app");
    private final DatabaseReference genreRef = database.getReference("genre");
    private final DatabaseReference castRef = database.getReference("cast");

    public SeriesAdapter(Context context, List<SeriesModel> seriesList, String userId) {
        this.context = context;
        this.seriesList = seriesList != null ? seriesList : new ArrayList<>();
        this.userId = userId;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        SeriesModel series = seriesList.get(position);
        holder.tvTitleAdapter.setText(series.getTitle());
        Glide.with(context).load(series.getCover()).into(holder.ivCoverAdapter);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);

            intent.putExtra("title", series.getTitle());
            intent.putExtra("synopsis", series.getSynopsis());
            intent.putExtra("cover", series.getCover());
            intent.putExtra("background", series.getBackground());
            intent.putExtra("trailer", series.getTrailer());
            intent.putExtra("id", series.getId());
            intent.putExtra("userId", userId);

            // Llamar al método para obtener los nombres de los géneros
            getGenreNamesByIds(series.getGenre(), genreNames -> {
                Bundle genreBundle = new Bundle();
                for (int j = 0; j < genreNames.size(); j++) {
                    genreBundle.putString("genre" + j, genreNames.get(j));
                }
                intent.putExtra("genre", genreBundle);

                // Continuar con el envío de episodios y elenco
                Map<String, EpisodeModel> episodesMap = series.getEpisode();
                Bundle episodesBundle = new Bundle();
                int index = 0;
                for (Map.Entry<String, EpisodeModel> entry : episodesMap.entrySet()) {
                    EpisodeModel episode = entry.getValue();
                    episodesBundle.putString("episode_id" + index, episode.getEpisodeId());
                    episodesBundle.putString("episode_title" + index, episode.getEpisodeTitle());
                    episodesBundle.putString("episode_url" + index, episode.getEpisodeUrl());
                    index++;
                }
                episodesBundle.putInt("episode_count", episodesMap.size());
                intent.putExtra("episode", episodesBundle);

                // Obtener el elenco y lanzar la actividad
                Map<String, String> castMap = series.getCast();
                Bundle castBundle = new Bundle();
                int[] i = {0};
                for (Map.Entry<String, String> entry : castMap.entrySet()) {
                    String castId = entry.getValue();
                    getCastModelById(castId, castModel -> {
                        if (castModel != null) {
                            castBundle.putString("c_name" + i[0], castModel.getName());
                            castBundle.putString("c_url" + i[0], castModel.getUrl());
                        }
                        i[0]++;
                        if (i[0] == castMap.size()) {
                            intent.putExtra("cast", castBundle);
                            context.startActivity(intent);
                        }
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public static class SeriesViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCoverAdapter;
        TextView tvTitleAdapter;

        public SeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCoverAdapter = itemView.findViewById(R.id.ivCoverAdapter);
            tvTitleAdapter = itemView.findViewById(R.id.tvTitleAdapter);
        }
    }

    public interface GenreNamesCallback {
        void onCallback(List<String> genreNames);
    }

    private void getGenreNamesByIds(Map<String, String> genreMap, GenreNamesCallback callback) {
        genreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> genreNames = new ArrayList<>();
                for (Map.Entry<String, String> entry : genreMap.entrySet()) {
                    String genreKey = entry.getKey();
                    if (snapshot.hasChild(genreKey)) {
                        String genreName = snapshot.child(genreKey).child("g_name").getValue(String.class);
                        if (genreName != null) {
                            genreNames.add(genreName);
                        }
                    }
                }
                callback.onCallback(genreNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public interface CastModelCallback {
        void onCallback(CastModel castModel);
    }

    private void getCastModelById(String castId, CastModelCallback callback) {
        castRef.child(castId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("c_name").getValue(String.class);
                    String url = snapshot.child("c_url").getValue(String.class);
                    callback.onCallback(new CastModel(name, url));
                } else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }
}
