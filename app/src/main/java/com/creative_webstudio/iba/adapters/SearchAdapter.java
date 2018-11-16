package com.creative_webstudio.iba.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.vieholders.SearchViewHolder;
import com.creative_webstudio.iba.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private ProductDelegate mproductDelegate;
    private List<NamesVo> names;


    public SearchAdapter(ProductDelegate productDelegate, List<NamesVo> names) {
        this.names = names;
        this.mproductDelegate = productDelegate;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_holder_search_list, parent, false);
        return new SearchViewHolder(view, mproductDelegate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder viewHolder, int i) {
        viewHolder.setNames(names.get(i));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void updateProductList(List<NamesVo> newList) {
        names = new ArrayList<>();
        names.addAll(newList);
        notifyDataSetChanged();
    }
}
