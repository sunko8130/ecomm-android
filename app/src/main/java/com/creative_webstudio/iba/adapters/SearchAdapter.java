package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductSearchActivity;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;


import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class SearchAdapter extends BaseRecyclerAdapter<SearchAdapter.SearchViewHolder, ProductVO> {

    private ProductSearchDelegate mproductDelegate;
    Context mContext;


    public SearchAdapter(Context context) {
        super(context);
        this.mContext = context;
//        this.mproductDelegate = productDelegate;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_search_list, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    public class SearchViewHolder extends BaseViewHolder<ProductVO> {

        @BindView(R.id.tv_product_name)
        MMTextView tvProductName;
        @BindView(R.id.iv_product)
        ImageView ivProduct;
        // private ProductSearchDelegate mSearchDelegate;
        private ProductVO productVO;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            // mSearchDelegate = productDelegate;

//            itemView.setOnClickListener(view -> ((ProductSearchActivity) mContext).onItemClick(productVO));

        }


        @Override
        public void setData(ProductVO data) {
            tvProductName.setText(data.getProductName());
            productVO = data;
        }

        @Override
        public void onClick(View v) {
            ((ProductSearchActivity) mContext).onItemClick(productVO);

        }

    }


}
