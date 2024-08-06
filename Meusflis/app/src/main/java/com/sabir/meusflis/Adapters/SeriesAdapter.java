package com.sabir.meusflis.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sabir.meusflis.Activities.DetailsActivity;
import com.sabir.meusflis.R;
import com.sabir.meusflis.Models.SeriesModel;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private List<SeriesModel> modelList;

    public SeriesAdapter(List<SeriesModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public SeriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesAdapter.MyViewHolder holder, int position) {
        holder.title.setText(modelList.get(position).getTitle());
        Glide.with(holder.imageView.getContext()).load(modelList.get(position).getCover()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendDataToDetailsActivity = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                sendDataToDetailsActivity.putExtra("title", modelList.get(position).getTitle());
                sendDataToDetailsActivity.putExtra("episodes", modelList.get(position).getEpisodes());
                sendDataToDetailsActivity.putExtra("background", modelList.get(position).getBackground());
                sendDataToDetailsActivity.putExtra("cover", modelList.get(position).getCover());
                sendDataToDetailsActivity.putExtra("synopsis", modelList.get(position).getSynopsis());
                sendDataToDetailsActivity.putExtra("cast", modelList.get(position).getCast());
                sendDataToDetailsActivity.putExtra("trailer", modelList.get(position).getTrailer());

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)holder.itemView.getContext(), holder.imageView, "imageMain");

                holder.itemView.getContext().startActivity(sendDataToDetailsActivity, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
