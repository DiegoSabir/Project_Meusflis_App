package com.sabir.meusflis.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.MyViewHolderAll> implements Filterable {

    private Context context;
    private ArrayList<SeriesModel> seriesFilter;
    private AllFilter filter;
    List<SeriesModel> seriesList;
    int height, width;
    private String userId;

    public AllAdapter(Context context, List<SeriesModel> seriesList, String userId) {
        this.context = context;
        this.seriesList = seriesList;
        this.seriesFilter = (ArrayList<SeriesModel>) seriesList;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyViewHolderAll onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_rv_all, parent, false);
        return new MyViewHolderAll(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderAll holder, int position) {
        SeriesModel series = seriesList.get(position);
        holder.tvAll.setText(series.getTitle());
        Glide.with(context).load(series.getCover()).into(holder.ivAll);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);

            intent.putExtra("title", series.getTitle());
            intent.putExtra("synopsis", series.getSynopsis());
            intent.putExtra("cover", series.getCover());
            intent.putExtra("background", series.getBackground());
            intent.putExtra("trailer", series.getTrailer());
            intent.putExtra("id", series.getId());

            intent.putExtra("userId", userId);

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
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public static class MyViewHolderAll extends RecyclerView.ViewHolder{
        ImageView ivAll;
        TextView tvAll;

        public MyViewHolderAll(@NonNull @NotNull View itemView){
            super(itemView);
            ivAll = itemView.findViewById(R.id.ivAll);
            tvAll = itemView.findViewById(R.id.tvAll);
        }
    }

    private void getCastModelById(String castId, SeriesAdapter.CastModelCallback callback) {
        DatabaseReference castRef = FirebaseDatabase.getInstance("https://meusflis-c2586-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("cast").child(castId);

        castRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("c_name").getValue(String.class);
                    String url = snapshot.child("c_url").getValue(String.class);
                    callback.onCallback(new CastModel(name, url));
                }
                else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCallback(null);
            }
        });
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new AllFilter(this, seriesFilter);
        }
        return filter;
    }
}
