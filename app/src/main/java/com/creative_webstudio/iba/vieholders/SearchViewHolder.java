package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.datas.vos.NamesVo;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;


public class SearchViewHolder extends BaseViewHolder<NamesVo> {

    @BindView(R.id.tv_product_name)
    MMTextView tvProductName;
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    private ProductSearchDelegate mSearchDelegate;

    public SearchViewHolder(@NonNull View itemView, final ProductSearchDelegate productDelegate) {
        super(itemView);
        mSearchDelegate=productDelegate;

    }

    @Override
    public void setData(NamesVo data) {
        tvProductName.setText(data.getName());
    }

    @Override
    public void onClick(View v) {
        mSearchDelegate.onTapView();
    }
}
