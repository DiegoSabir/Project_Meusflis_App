package com.example.meusflis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_catalogue,  parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElement> items){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDemographicCategories;
        TextView title, genre, author;
        ViewHolder(View itemView){
            super(itemView);
            ivDemographicCategories = itemView.findViewById(R.id.ivDemographicCategories);
            title = itemView.findViewById(R.id.tvTitle);
            genre = itemView.findViewById(R.id.tvGenre);
            author = itemView.findViewById(R.id.tvAuthor);
        }

        void bindData(final ListElement item){
            String imageName = "contact_mail_24dp";
            switch (item.getDemographicCategories()) {
                case "Kodomo":
                    imageName = "kodomo";
                    break;
                case "Josei":
                    imageName = "josei";
                    break;
                case "Seinen":
                    imageName = "seinen";
                    break;
                case "Shoujo":
                    imageName = "shoujo";
                    break;
                case "Shounen":
                    imageName = "shounen";
                    break;
            }
            int imageResource = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            ivDemographicCategories.setImageResource(imageResource);

            title.setText(item.getTitle());
            genre.setText(item.getGenre());
            author.setText(item.getAuthor());
        }
    }
}
