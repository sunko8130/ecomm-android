package com.creative_webstudio.iba.vieholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;


public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView tvProductName;
    private NamesVo mNames;
    private ImageView ivProduct;
    int width;


    public ProductViewHolder(@NonNull View itemView, final ProductDelegate productDelegate) {
        super(itemView);
        tvProductName = itemView.findViewById(R.id.tv_product_name);
        ivProduct = itemView.findViewById(R.id.iv_product);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDelegate.onTapView();
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
