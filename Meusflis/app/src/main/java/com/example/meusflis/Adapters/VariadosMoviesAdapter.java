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
import com.example.meusflis.Utils.ClassDialogs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VariadosMoviesAdapter extends RecyclerView.Adapter<VariadosMoviesAdapter.MyViewHolderV> {

    Context context;
    List<MoviesModel> mData;

    public VariadosMoviesAdapter(Context context, List<MoviesModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolderV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_movie_expanded, parent, false);

        return new MyViewHolderV(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderV holder, int position) {

        MoviesModel model = mData.get(position);

        try{
            Picasso.get().load(mData.get(position).getImageMovie()).resize(720, 720).into(holder.imgMovie);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imgMovie);
        }

        holder.imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDialogs.showBottomDetailsMovie(context, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolderV extends RecyclerView.ViewHolder{

        ImageView imgMovie;

        public MyViewHolderV(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imageThumbMovieExpanded);
        }
    }
}
