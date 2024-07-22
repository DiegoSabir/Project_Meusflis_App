package com.example.meusflis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeMoviesAdapter extends RecyclerView.Adapter<HomeMoviesAdapter.MyViewHolder> {

    Context context;
    List<MoviesModel> mDataList;

    public HomeMoviesAdapter(Context context, List<MoviesModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_thumb_movie_home, parent, false);

        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try{
            Picasso.get().load(mDataList.get(position).getImageMovie()).resize(720, 720).onlyScaleDown().into(holder.imgMovie);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imgMovie);
        }

        holder.imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Mostrnado datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imageThumbMovieHome);
        }
    }
}
