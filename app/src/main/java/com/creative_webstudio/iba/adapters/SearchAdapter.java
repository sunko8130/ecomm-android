package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductSearchActivity;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;


import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class SearchAdapter extends BaseRecyclerAdapter<SearchAdapter.SearchViewHolder, ProductVO> {

    private ProductSearchDelegate mproductDelegate;
    Context mContext;
    IBAPreferenceManager mIbaShared;

    public SearchAdapter(Context context) {
        super(context);
        this.mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
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
        @BindView(R.id.tv_price)
        MMTextView tvMMK;

        // private ProductSearchDelegate mSearchDelegate;
        private ProductVO productVO;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

        }


        @Override
        public void setData(ProductVO data) {
            tvProductName.setText(data.getProductName());
            if (data.getThumbnailIdsList().size() > 0) {
                GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), data.getThumbnailIdsList().get(0));
                Glide.with(itemView.getContext())
                        .asBitmap()
                        .apply(LoadImage.getOption())
                        .load(glideUrl)
                        .into(ivProduct);
            }
            productVO = data;
            OrderUnitVO order = data.getOrderUnits().get(0);
            tvMMK.setText(String.valueOf(order.getPricePerUnit()) + " MMK -( 1" + order.getUnitName() + " per " + order.getItemsPerUnit() + " " + order.getItemName() + ")");
        }

        @Override
        public void onClick(View v) {
            ((ProductSearchActivity) mContext).onItemClick(productVO);

        }

    }


}
