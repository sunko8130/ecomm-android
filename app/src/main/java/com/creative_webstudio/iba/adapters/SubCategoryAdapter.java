package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.MainActivity;
import com.creative_webstudio.iba.activities.ProductShowActivity;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends BaseRecyclerAdapter<SubCategoryAdapter.SubCategoryViewHolder, CategoryVO> {

    IBAPreferenceManager mIbaShared;
    Context mContext;

    public SubCategoryAdapter(Context context) {
        super(context);
        mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_sub_category, parent, false);
        return new SubCategoryViewHolder(view);
    }

    public class SubCategoryViewHolder extends BaseViewHolder<CategoryVO> {

        @BindView(R.id.tvCateName)
        MMTextView tvCateName;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.layoutCate)
        RelativeLayout layoutCate;

        private CategoryVO categoryVO;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(CategoryVO data) {
            categoryVO = data;
            tvCateName.setText(data.getName());
            ivImage.setVisibility(View.VISIBLE);
            if (data.getThumbnailId() != null) {
                GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), data.getThumbnailId());
                Glide.with(itemView.getContext())
                        .asBitmap()
                        .apply(LoadImage.getOption())
                        .load(glideUrl)
                        .into(ivImage);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.blank_photo)
                        .into(ivImage);
            }

            if (data.isSelected()) {
                layoutCate.setVisibility(View.VISIBLE);
            } else {
                layoutCate.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            ((ProductShowActivity) mContext).onSubItemClick(categoryVO);
        }
    }
}
