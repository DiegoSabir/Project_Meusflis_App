package com.example.meusflis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Models.MoviesCategoriesModel;
import com.example.meusflis.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoviesCategoriesAdapter extends RecyclerView.Adapter<MoviesCategoriesAdapter.MyViewHolderCategory> {

    Context context;
    List<MoviesCategoriesModel> mData;
    EventListener eventListener;

    public MoviesCategoriesAdapter(Context context, List<MoviesCategoriesModel> mData, EventListener eventListener) {
        this.context = context;
        this.mData = mData;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public MoviesCategoriesAdapter.MyViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_list_categories, parent, false);

        return new MyViewHolderCategory(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesCategoriesAdapter.MyViewHolderCategory holder, int position) {

        holder.txtNameCategories.setText(mData.get(position).getName());

        int paddingDp = 100;

        float density = context.getResources().getDisplayMetrics().density;

        int paddingPixel = (int)(paddingDp*density);

        if (position == 0){
            holder.txtNameCategories.setPadding(0, paddingPixel, 0, 0);
        }

        if (position == mData.size() - 1){
            holder.txtNameCategories.setPadding(0, 0 , 0, paddingPixel);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onEventName(mData.get(position).getName());
            }
        });
    }

    public interface EventListener{
        void onEventName(String nombre);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolderCategory extends RecyclerView.ViewHolder{

        TextView txtNameCategories;

        public MyViewHolderCategory(@NonNull @NotNull View itemView){
            super(itemView);

            txtNameCategories = itemView.findViewById(R.id.txtNameCategory);
        }
    }
}
