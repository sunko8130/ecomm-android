package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.datas.vos.NamesVo;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;


public class SearchViewHolder extends BaseViewHolder<ProductVO> {

    @BindView(R.id.tv_product_name)
    MMTextView tvProductName;
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    private ProductSearchDelegate mSearchDelegate;

    public SearchViewHolder(@NonNull View itemView, final ProductSearchDelegate productDelegate) {
        super(itemView);
        mSearchDelegate = productDelegate;

    }

    @Override
    public void onClick(View v) {
        mSearchDelegate.onTapView();
    }

    @Override
    public void setData(ProductVO data) {
        tvProductName.setText(data.getProductName());
    }
}
