package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.PromoRewardVO;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class PromoAdapter extends BaseRecyclerAdapter<PromoAdapter.PromoViewHolder, PromoRewardVO> {

    public PromoAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_promo_list, parent, false);
        return new PromoViewHolder(view);
    }

    public class PromoViewHolder extends BaseViewHolder<PromoRewardVO> {

        @BindView(R.id.tvQuantity)
        MMTextView tvQuantity;

        @BindView(R.id.tvPromo)
        MMTextView tvPromo;

        @BindView(R.id.ivCheck)
        ImageView ivCheck;

        public PromoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(PromoRewardVO data) {
            tvQuantity.setText("Buy - " + data.getPromoQuantity()+" ("+data.getShowUnit()+")");
            switch (data.getRewardType()) {
                case "Percentage":
                    tvPromo.setText("Save - "+data.getDiscountValue()+" %");
                    break;
                case "Fixed Amount":
                    String s = String.format("$%,.2f", data.getDiscountValue());
                    tvPromo.setText("Save - "+s+" MMK");
                    break;
                default:
                    tvPromo.setText("Get - "+data.getRewardName());
                    break;
            }
            if(data.getQuantity()<data.getPromoQuantity()){
                ivCheck.setImageResource(R.drawable.ic_delete);
            }else {
                ivCheck.setImageResource(R.drawable.ic_promo_get);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
