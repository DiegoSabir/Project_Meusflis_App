package com.example.meusflis;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<MultimediaContent> items;
    private LayoutInflater mInflater;
    private Context context;
    private List<MultimediaContent> originalItems;

    public ListAdapter(List<MultimediaContent> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = itemList;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(items);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void searchFilter(String strSearch){
        if (strSearch.length() == 0){
            items.clear();
            items.addAll(originalItems);
        }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                List<MultimediaContent> collect = items.stream()
                        .filter(i -> i.getTitle().contains(strSearch))
                        .collect(Collectors.toList());
                items.clear();
                items.addAll(collect);
            }
            else{
                items.clear();
                for (MultimediaContent i: originalItems) {
                    if (i.getTitle().contains(strSearch)){
                        items.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_catalogue,  parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.bindData(items.get(position));
    }

    public void setItems(List<MultimediaContent> items){
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        ImageView ivDemographicCategories;
        TextView title, genre, author;

        ViewHolder(View itemView){
            super(itemView);
            ivDemographicCategories = itemView.findViewById(R.id.ivDemographicCategories);
            title = itemView.findViewById(R.id.tvTitle);
            genre = itemView.findViewById(R.id.tvGenre);
            author = itemView.findViewById(R.id.tvAuthor);

            itemView.setOnCreateContextMenuListener(this);
        }

        void bindData(final MultimediaContent item){
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(R.menu.contextual_menu, menu);
        }
    }

    public static void handleContextMenuSelection(MenuItem item, Context context) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.ctx_manga:

                break;

            case R.id.ctx_anime:

                break;

            case R.id.ctx_light_novel:
                
                break;
        }
    }
}
