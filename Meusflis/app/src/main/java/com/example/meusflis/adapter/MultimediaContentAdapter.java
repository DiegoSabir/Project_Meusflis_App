package com.example.meusflis.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meusflis.R;
import com.example.meusflis.activity.ForgetActivity;
import com.example.meusflis.activity.LoginActivity;
import com.example.meusflis.model.MultimediaContent;

import java.util.ArrayList;

public class MultimediaContentAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<MultimediaContent> arrayMultimediaContent;

    public MultimediaContentAdapter(Context context, ArrayList<MultimediaContent> arrayMultimediaContent) {
        super(context, R.layout.listview_item, arrayMultimediaContent);
        this.context = context;
        this.arrayMultimediaContent = arrayMultimediaContent;
    }

    static class ViewHolder{
        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvGenre;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.ivCover = convertView.findViewById(R.id.ivCover);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            viewHolder.tvGenre = convertView.findViewById(R.id.tvGenre);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MultimediaContent multimediaContent = arrayMultimediaContent.get(position);

        viewHolder.ivCover.setImageBitmap(multimediaContent.getCover());
        viewHolder.tvTitle.setText(multimediaContent.getTitle());
        viewHolder.tvAuthor.setText(multimediaContent.getAuthor());
        viewHolder.tvGenre.setText(multimediaContent.getGenre());

        return convertView;
    }
}
