package com.example.meusflis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meusflis.R;
import com.example.meusflis.model.Episode;

import java.util.ArrayList;

public class EpisodeAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Episode> arrayEpisode;

    public EpisodeAdapter(Context context, ArrayList<Episode> arrayEpisode) {
        super(context, R.layout.listview_episode, arrayEpisode);
        this.context = context;
        this.arrayEpisode = arrayEpisode;
    }

    static class ViewHolder{
        TextView tvNumberEpisode;
        TextView tvTitleEpisode;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        EpisodeAdapter.ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_episode, parent, false);

            viewHolder = new EpisodeAdapter.ViewHolder();

            viewHolder.tvNumberEpisode = convertView.findViewById(R.id.tvNumberEpisode);
            viewHolder.tvTitleEpisode = convertView.findViewById(R.id.tvTitleEpisode);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (EpisodeAdapter.ViewHolder) convertView.getTag();
        }

        Episode episode = arrayEpisode.get(position);

        viewHolder.tvNumberEpisode.setText(episode.getEpisodeNumber());
        viewHolder.tvTitleEpisode.setText(episode.getEpisodeTitle());

        return convertView;
    }
}
