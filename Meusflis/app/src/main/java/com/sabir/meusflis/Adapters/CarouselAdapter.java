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
import com.sabir.meusflis.Models.CarouselModel;
import com.sabir.meusflis.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends SliderViewAdapter<CarouselAdapter.MyViewHolder> {

    private Context context;

    public CarouselAdapter(Context context) {
        this.context = context;
    }

    private List<CarouselModel> carouselModels = new ArrayList<>();

    public void renewItems(List<CarouselModel> carouselModels){
        this.carouselModels = carouselModels;
        notifyDataSetChanged();
    }

    public void deleteItems(int position){
        this.carouselModels.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.trailer_title.setText(carouselModels.get(position).getTtitle());

        Glide.with(viewHolder.itemView)
                .load(carouselModels.get(position).getTurl())
                .into(viewHolder.slider_image);

        viewHolder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trailer_video = new Intent(context, PlayerActivity.class);
                trailer_video.putExtra("vid", carouselModels.get(position).getTvid());
                trailer_video.putExtra("title", carouselModels.get(position).getTtitle());
                v.getContext().startActivity(trailer_video);
            }
        });
    }

    @Override
    public int getCount() {
        return carouselModels.size();
    }

    public class MyViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView slider_image;
        TextView trailer_title;
        FloatingActionButton play_button;

        public MyViewHolder(View itemView) {
            super(itemView);

            slider_image = itemView.findViewById(R.id.image_thumbnail);
            trailer_title = itemView.findViewById(R.id.trailer_title);
            play_button = itemView.findViewById(R.id.floatingActionButton);
        }
    }
}
