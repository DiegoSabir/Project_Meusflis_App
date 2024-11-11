package com.sabir.meusflis.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sabir.meusflis.Models.EpisodeModel;
import com.sabir.meusflis.Activities.PlayerActivity;
import com.sabir.meusflis.R;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {

    private List<EpisodeModel> episodeModels;

    public EpisodeAdapter(List<EpisodeModel> episodeModels) {
        this.episodeModels = episodeModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemEpisodeTitle.setText(episodeModels.get(position).getTitleEpisode());
        Glide.with(holder.itemView).load(episodeModels.get(position).getImageEpisode()).into(holder.itemEpisodeImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayerActivity.class);
            intent.putExtra("title", episodeModels.get(position).getTitleEpisode());
            intent.putExtra("vid", episodeModels.get(position).getVideoEpisode());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return episodeModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView itemEpisodeImage;
        TextView itemEpisodeTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemEpisodeImage = itemView.findViewById(R.id.itemEpisodeImage);
            itemEpisodeTitle = itemView.findViewById(R.id.itemEpisodeTitle);
        }
    }
}
