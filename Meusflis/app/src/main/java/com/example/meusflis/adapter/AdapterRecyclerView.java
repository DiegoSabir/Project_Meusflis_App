package com.example.meusflis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.R;
import com.example.meusflis.model.MultimediaContent;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {
    Context context;
    ArrayList<MultimediaContent> multimediaContentsArrayList;

    public AdapterRecyclerView(Context context, ArrayList<MultimediaContent> multimediaContentsArrayList){
        this.context = context;
        this.multimediaContentsArrayList = multimediaContentsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_catalogue, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultimediaContent multimediaContent = multimediaContentsArrayList.get(position);
        //holder.ivCover.setImageResource(multimediaContent.);
        holder.tvTitle.setText(multimediaContent.title);
        holder.tvAuthor.setText(multimediaContent.author);
        holder.tvGenre.setText(multimediaContent.genre);
        holder.ivDemographicCategories.setImageResource(multimediaContent.demographicCategories);
    }

    @Override
    public int getItemCount() {
        return multimediaContentsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvGenre;
        ImageView ivDemographicCategories;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvGenre = itemView.findViewById(R.id.tvGenre);
            ivDemographicCategories = itemView.findViewById(R.id.ivDemographicCategories);
        }
    }
}
