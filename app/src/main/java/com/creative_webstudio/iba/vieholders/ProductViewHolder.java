package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductActivity;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;


public class ProductViewHolder extends BaseViewHolder<ProductVO> {
    @BindView(R.id.tv_product_name)
    MMTextView tvProductName;

    @BindView(R.id.iv_product)
    ImageView ivProduct;

    private ProductDelegate mDelecate;
    private ProductVO productVO;

    public ProductViewHolder(@NonNull View itemView, final ProductDelegate productDelegate) {
        super(itemView);
        mDelecate=productDelegate;

    }


    @Override
    public void setData(ProductVO data) {
        tvProductName.setText(data.getProductName());
//        if(data.getImage()!=null){
//            Glide.with(itemView.getContext())
//                    .load(data.getImage())
//                    .into(ivProduct);
//        }
        productVO =data;
    }

    @Override
    public void onClick(View v) {
        mDelecate.onTapView(productVO.getId());
    }
}
