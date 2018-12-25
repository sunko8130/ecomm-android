package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductActivity;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import org.mmtextview.components.MMTextView;

import java.util.List;

import butterknife.BindView;


public class ProductAdapter extends BaseRecyclerAdapter<ProductAdapter.ProductViewHolder,ProductVO> {

    private ProductDelegate mProductDelegate;
    Context mContext;
    protected List<ProductVO> mData;
    IBAPreferenceManager mIbaShared;


    public ProductAdapter(Context context, ProductDelegate productDelegate) {
        super(context);
        this.mProductDelegate = productDelegate;
    }

    public ProductAdapter(Context context) {
        super(context);
        mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_product_list, parent, false);
        return new ProductViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(ProductViewHolder holder, int position) {
//        holder.setData(mData.get(position));
//        if (mContext instanceof ProductActivity) {
//            ((ProductActivity) mContext).onItemClick(mData.get(position));
//        }
//    }

    public class ProductViewHolder extends BaseViewHolder<ProductVO> {
        @BindView(R.id.tv_product_name)
        MMTextView tvProductName;

        @BindView(R.id.iv_product)
        ImageView ivProduct;


        private ProductVO productVO;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setData(ProductVO data) {
            tvProductName.setText(data.getProductName());
            if(!data.getThumbnailIdsList().isEmpty()){
                GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(),data.getThumbnailIdsList().get(0));
                Glide.with(itemView.getContext())
                        .asBitmap()
                        .apply(LoadImage.getOption())
                        .load(glideUrl)
                        .into(ivProduct);
            }

            productVO =data;
        }

        @Override
        public void onClick(View v) {
            ((ProductActivity) mContext).onItemClick(productVO);
        }
    }


}
