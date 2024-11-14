package com.sabir.meusflis.Adapters;

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

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private List<CastModel> castModels;

    public CastAdapter(List<CastModel> castModels) {
        this.castModels = castModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemCastName.setText(castModels.get(position).getNameCast());

        String imageUrl = "https://drive.google.com/uc?export=download&id=" + castModels.get(position).getImageCast();
        Glide.with(holder.itemCastImage.getContext()).load(imageUrl).into(holder.itemCastImage);
    }

    @Override
    public int getItemCount() {
        return castModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView itemCastImage;
        TextView itemCastName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCastImage = itemView.findViewById(R.id.itemCastImage);
            itemCastName = itemView.findViewById(R.id.itemCastName);
        }
    }
}
