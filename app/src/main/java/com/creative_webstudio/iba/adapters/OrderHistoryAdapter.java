package com.creative_webstudio.iba.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creative_webstudio.iba.R;

public class OrderHistoryAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // TODO:
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        public OrderHistoryViewHolder(View itemView) {
            super(itemView);

            // TODO:
        }
    }
}
