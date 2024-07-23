package com.example.meusflis.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.example.meusflis.Utils.ClassDialogs;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.MyViewHolderMovies> implements Filterable {

    Context context;
    public ArrayList<MoviesModel> mDataFilter;
    MovieFilter filter;
    List<MoviesModel> mDataList;
    int height, width;

    public AllMoviesAdapter(Context context, List<MoviesModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        this.mDataFilter = (ArrayList<MoviesModel>)mDataList;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @NonNull
    @Override
    public AllMoviesAdapter.MyViewHolderMovies onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_thumb_movie, parent, false);

        return new MyViewHolderMovies(root);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMoviesAdapter.MyViewHolderMovies holder, int position) {

        try{
            Picasso.get().load(mDataList.get(position).getImageMovie()).placeholder(R.color.colorDeCarga).error(R.color.colorDeCarga).resize(720, 720).onlyScaleDown().into(holder.imgMovie);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imgMovie);
        }

        holder.cardViewContainerImageThumbMovie.getLayoutParams().height = (height / 3) - 70;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDialogs.showBottomDetailsMovie(context, mDataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public Filter getFilter() {

        if (filter == null){
            filter = new MovieFilter(this, mDataFilter);
        }

        return filter;
    }

    class MyViewHolderMovies extends RecyclerView.ViewHolder{

        ImageView imgMovie;
        CardView cardViewContainerImageThumbMovie;

        public MyViewHolderMovies(@NonNull @NotNull View itemView){
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imageThumbMovie);
            cardViewContainerImageThumbMovie = itemView.findViewById(R.id.cardViewContainerImageThumbMovie);
        }
    }
}
