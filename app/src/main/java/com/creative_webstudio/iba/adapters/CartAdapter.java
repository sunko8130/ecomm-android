package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.CartActivity;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;


import java.util.List;

import butterknife.BindView;

public class CartAdapter extends BaseRecyclerAdapter<CartAdapter.CartViewHolder, CartShowVO> {

    Context mContext;
    IBAPreferenceManager mIbaShared;

    public CartAdapter(Context context) {
        super(context);
        mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_cart_list, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartShowVO cart = getItem(position);
                if (cart != null) {
                    ((CartActivity) mContext).onRemoveCart(cart);
                }
                //((CartActivity) mContext).onClickItem(1,null,position);
            }
        });
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
            tvPrice.setText(data.getPricePerUnit() + " MMK");
            tvQuantity.setText(data.getItemQuantity() + " " + data.getUnitShow());
            GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), data.getThumbnailId());
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .apply(LoadImage.getOption())
                    .load(glideUrl)
                    .into(ivProduct);
        }

        @Override
        public void onClick(View view) {

        }
    }
}


