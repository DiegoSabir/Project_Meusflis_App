package com.sabir.meusflis.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sabir.meusflis.Activities.PlayerActivity;
import com.sabir.meusflis.Models.SeriesModel;
import com.sabir.meusflis.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder> {

    private Context context;
    private List<SeriesModel> seriesModels = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<SeriesModel> sliderItems) {
        this.seriesModels = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItems(int position) {
        this.seriesModels.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.itemSliderTitle.setText(seriesModels.get(position).getTitle());

        String coverUrl = "https://drive.google.com/uc?export=download&id=" + seriesModels.get(position).getCover();
        Glide.with(viewHolder.itemSliderCover.getContext()).load(coverUrl).into(viewHolder.itemSliderCover);

        viewHolder.itemSliderTrailer.setOnClickListener(v -> {
            Intent trailer_video = new Intent(context, PlayerActivity.class);
            trailer_video.putExtra("vid", seriesModels.get(position).getTrailer());
            trailer_video.putExtra("title", seriesModels.get(position).getTitle());
            v.getContext().startActivity(trailer_video);
        });
    }

    @Override
    public int getCount() {
        return seriesModels.size();
    }

    public class MyViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView itemSliderCover;
        TextView itemSliderTitle;
        FloatingActionButton itemSliderTrailer;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemSliderCover = itemView.findViewById(R.id.itemSliderCover);
            itemSliderTitle = itemView.findViewById(R.id.itemSliderTitle);
            itemSliderTrailer = itemView.findViewById(R.id.itemSliderTrailer);
        }
    }
}
