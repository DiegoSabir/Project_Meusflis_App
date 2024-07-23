package com.example.meusflis.Adapters;

import android.widget.Filter;

import com.example.meusflis.Common.Common;
import com.example.meusflis.Models.MoviesModel;

import java.util.ArrayList;

public class MovieFilter extends Filter {

    AllMoviesAdapter adapter;
    ArrayList<MoviesModel> filterList;

    public MovieFilter(AllMoviesAdapter adapter, ArrayList<MoviesModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null){
            constraint = constraint.toString().toUpperCase();
            ArrayList<MoviesModel> filteredModels = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++){

                if (Common.getCategorySelected().equals("") || Common.getCategorySelected().equals("TODAS LAS CATEGORIAS")){

                    if (filterList.get(i).getTitleMovie().toUpperCase().contains(constraint)){
                        filteredModels.add(filterList.get(i));
                    }
                }
                else{
                    if(filterList.get(i).getGenderMovie().toUpperCase().equals(Common.getCategorySelected())){

                        if(filterList.get(i).getTitleMovie().toUpperCase().contains(constraint)){
                            filteredModels.add(filterList.get(i));
                        }
                    }
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.mDataList = (ArrayList<MoviesModel>)results.values;
        adapter.notifyDataSetChanged();

    }
}
