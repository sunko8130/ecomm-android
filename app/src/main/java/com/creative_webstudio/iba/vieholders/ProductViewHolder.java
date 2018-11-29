package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import butterknife.BindView;


public class ProductViewHolder extends BaseViewHolder<NamesVo> {
    @BindView(R.id.tv_product_name)
    TextView tvProductName;

    @BindView(R.id.iv_product)
    ImageView ivProduct;

    private ProductDelegate mDelecate;

    public ProductViewHolder(@NonNull View itemView, final ProductDelegate productDelegate) {
        super(itemView);
        mDelecate=productDelegate;
    }


    @Override
    public void setData(NamesVo data) {
        tvProductName.setText(data.getName());
    }

    @Override
    public void onClick(View v) {
        mDelecate.onTapView();
    }
}
