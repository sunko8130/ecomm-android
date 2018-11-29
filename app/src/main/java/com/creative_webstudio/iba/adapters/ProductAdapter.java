package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.vieholders.ProductViewHolder;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;

import java.util.List;


public class ProductAdapter extends BaseRecyclerAdapter<ProductViewHolder,NamesVo> {

    private ProductDelegate mproductDelegate;
    private List<NamesVo> names;


    public ProductAdapter(Context context,ProductDelegate productDelegate) {
        super(context);
        this.mproductDelegate = productDelegate;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_product_list, parent, false);
        return new ProductViewHolder(view, mproductDelegate);
    }

}
