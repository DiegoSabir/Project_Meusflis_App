package com.sabir.meusflis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sabir.meusflis.Models.CastModel;
import com.sabir.meusflis.R;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<CastModel> castList;

    public CastAdapter(List<CastModel> castList) {
        this.castList = castList != null ? castList : new ArrayList<>();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        holder.tvCastName.setText(castList.get(position).getName());
        Glide.with(holder.itemView).load(castList.get(position).getUrl()).into(holder.ivCastImage);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCastImage;
        TextView tvCastName;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCastImage = itemView.findViewById(R.id.ivCastImage);
            tvCastName = itemView.findViewById(R.id.tvCastName);
        }
    }
}
