package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.OrderHistoryActivity;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class OrderHistoryAdapter extends BaseRecyclerAdapter<OrderHistoryAdapter.OrderHistoryViewHolder,OrderHistoryVO> {

    private Context mContext;
    public OrderHistoryAdapter(Context context) {
        super(context);
        mContext=context;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }


    public class OrderHistoryViewHolder extends BaseViewHolder<OrderHistoryVO> {

        @BindView(R.id.tvDate)
        MMTextView tvDate;

        @BindView(R.id.tvOrderPrice)
        MMTextView tvOrderPrice;

        @BindView(R.id.tvStatus)
        MMTextView tvStatus;

        @BindView(R.id.tvOrderId)
        MMTextView tvOrderId;


        OrderHistoryVO historyVO;
        public OrderHistoryViewHolder(View itemView) {
            super(itemView);
            // TODO:
        }

        @Override
        public void setData(OrderHistoryVO data) {
            tvDate.setText(data.getOrderDate());
            if (data.getStatus().equals("Pending")) {
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.indianRed));
            } else if (data.getStatus().equals("Cancel")){
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.redFull));
            }else {
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.limeGreen));
            }
            tvStatus.setText(data.getStatus());
            long price=0;
            for (OrderItemVO order:data.getOrderItems()){
                price+=order.getOrderPrice();
            }
            tvOrderPrice.setText(price+" MMK");
            tvOrderId.setText(data.getOrderNumber());
            historyVO = data;
        }

        @Override
        public void onClick(View view) {
            ((OrderHistoryActivity) mContext).onItemClick(historyVO);
        }
    }
}
