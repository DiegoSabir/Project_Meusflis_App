package com.sabir.meusflis.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabir.meusflis.Activities.PlayerActivity;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.R;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private final Context context;
    private final List<EpisodeModel> episodeList;

    public EpisodeAdapter(Context context, List<EpisodeModel> episodeList) {
        this.context = context;
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.episode_item, parent, false);
        return new EpisodeViewHolder(view);
    }

    public void updateEpisodes(List<EpisodeModel> newEpisodes) {
        episodeList.clear();
        episodeList.addAll(newEpisodes);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        EpisodeModel episode = episodeList.get(position);
        holder.tvEpisodeTitle.setText(episode.getEpisodeTitle());

        // Agregar un OnClickListener para manejar el clic en cada episodio
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("vid", episode.getEpisodeUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEpisodeTitle;
        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEpisodeTitle = itemView.findViewById(R.id.tvEpisodeTitle);
        }
    }
}
