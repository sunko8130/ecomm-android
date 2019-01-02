package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.CartEditActivity;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMCheckBox;
import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class CartEditAdapter extends BaseRecyclerAdapter<CartEditAdapter.CartEditViewHolder, CartShowVO> {

    Context mContext;
    IBAPreferenceManager mIbaShared;
    public CartEditAdapter(Context context) {
        super(context);
        mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
    }

    @NonNull
    @Override
    public CartEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_cart_edit, parent, false);
        return new CartEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartEditViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CartShowVO cart = getItem(position);
        final int[] quantity = {0};
        quantity[0] = cart.getItemQuantity();
        holder.btnDelete.setOnClickListener(view -> {
            if (cart != null) {
                ((CartEditActivity) mContext).updateOrder(position,-1,cart);
            }
        });
        holder.ivPlus.setOnClickListener(v -> {
            if (quantity[0] < cart.getMax()) {
                quantity[0]++;
                ((CartEditActivity) mContext).updateOrder(position, quantity[0],cart);
                holder.edQuantity.setText(String.valueOf(quantity[0]));
                if(quantity[0]==cart.getMax()){
                    holder.ivPlus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.redFull)));
                }else {
                    holder.ivMinus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.blackFull)));
                }
            }
        });
        holder.ivMinus.setOnClickListener(v -> {
            if (quantity[0] > cart.getMin()) {
                quantity[0]--;
                ((CartEditActivity) mContext).updateOrder(position, quantity[0],cart);
                holder.edQuantity.setText(String.valueOf(quantity[0]));
                if (quantity[0] <= cart.getMax()) {
                    holder.edQuantity.setTextColor(ContextCompat.getColor(mContext, R.color.limeGreen));
                }
                if(quantity[0]==cart.getMin()){
                    holder.ivMinus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.redFull)));
                }else {
                    holder.ivPlus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.blackFull)));
                }
            }
        });


    }

    public class CartEditViewHolder extends BaseViewHolder<CartShowVO> {
        @BindView(R.id.tv_product_name)
        MMTextView tvProductName;

        @BindView(R.id.iv_product)
        ImageView ivProduct;

        @BindView(R.id.tv_min_max)
        MMTextView tvMinMax;

        @BindView(R.id.iv_plus)
        ImageView ivPlus;

        @BindView(R.id.iv_minus)
        ImageView ivMinus;

        @BindView(R.id.ed_quantity)
        EditText edQuantity;

        @BindView(R.id.btn_delete)
        ImageView btnDelete;

        public CartEditViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(CartShowVO data) {
            tvProductName.setText(data.getProductName());
            tvMinMax.setText("Stock-> min: " + data.getMin() + " - max: " + data.getMax());

            GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), data.getThumbnailId());
            Glide.with(itemView.getContext())
                    .asBitmap()
                    .apply(LoadImage.getOption())
                    .load(glideUrl)
                    .into(ivProduct);
        }


        @Override
        public void onClick(View v) {

        }
    }
}
