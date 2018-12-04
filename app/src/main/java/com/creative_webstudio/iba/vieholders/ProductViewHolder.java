package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;


public class ProductViewHolder extends BaseViewHolder<HCInfoVO> {
    @BindView(R.id.tv_product_name)
    MMTextView tvProductName;

    @BindView(R.id.iv_product)
    ImageView ivProduct;

    private ProductDelegate mDelecate;
    private HCInfoVO hcInfoVO;

    public ProductViewHolder(@NonNull View itemView, final ProductDelegate productDelegate) {
        super(itemView);
        mDelecate=productDelegate;
    }


    @Override
    public void setData(HCInfoVO data) {
        tvProductName.setText(data.getTitle());
        if(data.getImage()!=null){
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .into(ivProduct);
        }
        hcInfoVO=data;
    }

    @Override
    public void onClick(View v) {
        mDelecate.onTapView(hcInfoVO.getId());
    }
}
