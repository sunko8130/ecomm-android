package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.OrderHistoryAdapter;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;

public class OrderHistoryActivity extends BaseActivity {
    RecyclerView mRecyclerView;
    OrderHistoryAdapter mAdapter;
    int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_order_history);
        mAdapter = new OrderHistoryAdapter();

        getOrderHistory(++mCurrentPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOrderHistory(int page) {
        ProductViewModel viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        viewModel.getOrderHistory(page).observe(this, response -> {

        });
    }
}
