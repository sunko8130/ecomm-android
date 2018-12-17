package com.creative_webstudio.iba.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.delegates.CartDelegate;
import com.creative_webstudio.iba.vieholders.CartViewHolder;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private CartDelegate mproductDelegate;


    public CartAdapter(List<String> names,CartDelegate productDelegate) {
        this.mproductDelegate = productDelegate;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_holder_cart_list, parent, false);
        return new CartViewHolder(view, mproductDelegate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder viewHolder, int i) {
        viewHolder.setNames(null);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public void updateProductList(List<String> newList) {
        notifyDataSetChanged();
    }
}
