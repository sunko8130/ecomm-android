package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.delegates.CartDelegate;
import com.creative_webstudio.iba.datas.vos.NamesVo;

import org.mmtextview.components.MMTextView;

public class CartViewHolder extends RecyclerView.ViewHolder {

    private MMTextView tvProductName;
    private NamesVo mNames;
    private ImageView ivProduct;
    int width;


    public CartViewHolder(@NonNull View itemView, final CartDelegate cartDelegate) {
        super(itemView);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        ivProduct = itemView.findViewById(R.id.iv_product);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDelegate.onTapView();
            }
        });

    }

    public void setNames(NamesVo names) {
        this.mNames = names;
        tvProductName.setText(names.getName());
        /*width = (BottomSheet.width / 2);
        ivProduct.getLayoutParams().width = Math.round(width / Resources.getSystem().getDisplayMetrics().density);
        ;*/

    }
}
