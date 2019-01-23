package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.ValueVo;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;

public class DetailAdapter extends BaseRecyclerAdapter<DetailAdapter.DetailViewHolder,ValueVo> {

    public DetailAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public DetailAdapter.DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_detail_list, parent, false);
        return new DetailAdapter.DetailViewHolder(view);
    }


    public class DetailViewHolder extends BaseViewHolder<ValueVo>{

        @BindView(R.id.tvTitle)
        MMTextView tvTitle;

        @BindView(R.id.tvDescription)
        MMTextView tvDescription;

        public DetailViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(ValueVo data) {
            tvTitle.setText(data.getTitle());
            tvDescription.setText(data.getDescription());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
