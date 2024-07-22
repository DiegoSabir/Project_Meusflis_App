package com.example.meusflis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.example.meusflis.Utils.ClassDialogs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoPrincipalAdapter extends RecyclerView.Adapter<VideoPrincipalAdapter.HolderVideo>{

    private Context context;
    public List<MoviesModel> videoPrincipalModels;

    public VideoPrincipalAdapter(Context context, List<MoviesModel> videoPrincipalModels) {
        this.context = context;
        this.videoPrincipalModels = videoPrincipalModels;
    }

    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed_header, parent, false);

        return new HolderVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {
        MoviesModel model = videoPrincipalModels.get(position);

        try {
            Picasso.get().load(model.getImageMovie()).resize(720, 720).onlyScaleDown().into(holder.imageVideoPrincipal);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imageVideoPrincipal);
        }

        holder.category.setText(new StringBuilder(model.getGenderMovie()).append(" | ").append(model.getTimeMovie()));

        holder.linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDialogs.shareMovieWithImage(context, holder.imageVideoPrincipal, model.getIdMovie(), model.getTitleMovie());
            }
        });

        holder.linearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDialogs.showBottomDetailsMovie(context, model);
            }
        });

        holder.linearPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Boton reproducir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class HolderVideo extends RecyclerView.ViewHolder{
        ImageView imageVideoPrincipal;
        LinearLayout linearPlay, linearInfo, linearShare;
        TextView category;

        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            imageVideoPrincipal = itemView.findViewById(R.id.background_image);
            linearPlay = itemView.findViewById(R.id.feed_play_button);
            linearInfo = itemView.findViewById(R.id.feed_info_button);
            linearShare = itemView.findViewById(R.id.linear_share_principal);
            category = itemView.findViewById(R.id.genero_text);
        }
    }
}
