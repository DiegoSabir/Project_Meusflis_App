package com.example.meusflis.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meusflis.Common.Common;
import com.example.meusflis.Models.MoviesModel;
import com.example.meusflis.R;
import com.example.meusflis.Utils.ClassDialogs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesTop10Adapter extends RecyclerView.Adapter<MoviesTop10Adapter.MyViewHolderT> {

    Context context;
    List<MoviesModel> mData;
    DatabaseReference reference;

    public MoviesTop10Adapter(Context context, List<MoviesModel> mData) {
        this.context = context;
        this.mData = mData;

        reference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_thumb_movie_top10, parent, false);

        return new MyViewHolderT(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderT holder, int position) {
        int pos = position + 1;

        holder.tvTop10.setText(String.valueOf(pos));

        reference.child(Common.NODO_MOVIES).child(Common.NODO_GENERAL).child(mData.get(position).getIdMovie())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            try{
                                Picasso.get().load(String.valueOf(snapshot.child("imageMovie").getValue())).resize(720, 720).onlyScaleDown().into(holder.imgMovie);
                            }
                            catch (Exception e){
                                Picasso.get().load(R.drawable.baseline_image_not_supported_24).into(holder.imgMovie);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.imgMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDialogs.showBottomDetailsMovieWithID(context, mData.get(position).getIdMovie());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolderT extends RecyclerView.ViewHolder{

        ImageView imgMovie;
        TextView tvTop10;

        public MyViewHolderT(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.imageThumbMovieTop10);
            tvTop10 = itemView.findViewById(R.id.tvTop10);
        }
    }
}
