package com.sabir.meusflis.Adapters;

import android.widget.Filter;

import com.sabir.meusflis.Common.Common;
import com.sabir.meusflis.Models.SeriesModel;

import java.util.ArrayList;

public class AllFilter extends Filter {

    AllAdapter adapter;
    ArrayList<SeriesModel> filterList;

    public AllFilter(AllAdapter adapter, ArrayList<SeriesModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if (constraint != null){
            constraint = constraint.toString().toUpperCase();
            ArrayList<SeriesModel> filteredModels = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++){
                if (Common.getCategorySelected().equals("") || Common.getCategorySelected().equals("TODAS LAS CATEGORIAS")){
                    if (filterList.get(i).getTitle().toUpperCase().contains(constraint)){
                        filteredModels.add(filterList.get(i));
                    }
                }
                else{
                    if(filterList.get(i).getGenre().equals(Common.getCategorySelected())){
                        if(filterList.get(i).getTitle().toUpperCase().contains(constraint)){
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
        adapter.seriesList = (ArrayList<SeriesModel>)results.values;
        adapter.notifyDataSetChanged();
    }
}
