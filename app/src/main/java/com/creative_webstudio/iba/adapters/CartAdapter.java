package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.CartActivity;
import com.creative_webstudio.iba.activities.ProductActivity;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;


import butterknife.BindView;

public class CartAdapter extends BaseRecyclerAdapter<CartAdapter.CartViewHolder, CartShowVO> {

    Context mContext;
    public CartAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_cart_list, parent, false);
        return new CartViewHolder(view);
    }

    public class CartViewHolder extends BaseViewHolder<CartShowVO> {

        @BindView(R.id.tv_product_name)
        MMTextView tvProductName;

        @BindView(R.id.iv_product)
        ImageView ivProduct;

        @BindView(R.id.tv_price)
        MMTextView tvPrice;

        @BindView(R.id.tv_Count)
        MMTextView tvQuantity;

        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void setData(CartShowVO data) {
            tvProductName.setText(data.getProductName());
            tvPrice.setText(data.getPricePerUnit()+" MMK");
            tvQuantity.setText(data.getItemQuantity()+" "+data.getUnitShow());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CartActivity) mContext).onClickItem(1,null);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}


