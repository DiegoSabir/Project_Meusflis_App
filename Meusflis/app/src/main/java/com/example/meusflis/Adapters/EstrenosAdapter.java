package com.example.meusflis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Models.EstrenosModel;
import com.example.meusflis.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EstrenosAdapter extends RecyclerView.Adapter<EstrenosAdapter.MyViewHolder> {

    private Context context;
    private List<EstrenosModel> mData;

    public EstrenosAdapter(Context context, List<EstrenosModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.comingsoon_item, parent, false);

        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull EstrenosAdapter.MyViewHolder holder, int position) {
        try{
            Picasso.get().load(mData.get(position).getImage()).resize(720, 720).onlyScaleDown().into(holder.imgMovie);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imgMovie);
        }

        holder.tvDay.setText(mData.get(position).getDay());
        holder.tvMonth.setText(new StringBuilder(mData.get(position).getMonth().substring(0,3).toUpperCase()).append("."));
        holder.tvDate.setText(new StringBuilder("Publicacion el ").append(mData.get(position).getDay()).append(" de ").append(mData.get(position).getMonth()));
        holder.tvSinopsis.setText(mData.get(position).getSinopsis());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imgMovie;
        TextView tvDay, tvMonth, tvSinopsis, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imgComingSoonPoster);
            tvDay = itemView.findViewById(R.id.tvComingSoonDay);
            tvMonth = itemView.findViewById(R.id.tvComingSoonMonth);
            tvSinopsis = itemView.findViewById(R.id.tvComingSoonSinopsis);
            tvDate = itemView.findViewById(R.id.tvComingSoonDate);
        }
    }
}
