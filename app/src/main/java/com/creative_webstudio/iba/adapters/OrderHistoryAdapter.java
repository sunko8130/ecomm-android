package com.creative_webstudio.iba.adapters;

import android.content.Context;
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
            tvDate.setText("Order Date: "+data.getOrderDate());
            if (data.getStatus().equals("Pending")) {
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.blue2));
            } else if (data.getStatus().equals("Completed")){
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.limeGreen));
            }else if(data.getStatus().equals("Shipped")){
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.orange));
            }else {
                tvStatus.setTextColor(ContextCompat.getColor(mContext,R.color.redFull));
            }
            tvStatus.setText(data.getStatus());
            double price=0;
            for (OrderItemVO order:data.getOrderItems()){
                price+=order.getOrderPrice();
            }
            tvOrderPrice.setText(String.format("%,.2f", price) + " Ks");
            tvOrderId.setText("Order ID: "+data.getOrderNumber());
            historyVO = data;
        }

        @Override
        public void onClick(View view) {
            ((OrderHistoryActivity) mContext).onItemClick(historyVO);
        }
    }
}
