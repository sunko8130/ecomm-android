package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.MainActivity;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends BaseRecyclerAdapter<CategoryAdapter.CategoryViewHolder, CategoryVO> {

    IBAPreferenceManager mIbaShared;
    Context mContext;

    public CategoryAdapter(Context context) {
        super(context);
        mContext = context;
        mIbaShared = new IBAPreferenceManager(mContext);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_category, parent, false);
        return new CategoryViewHolder(view);
    }

    public class CategoryViewHolder extends BaseViewHolder<CategoryVO> {

        @BindView(R.id.tvCateName)
        MMTextView tvCateName;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        private CategoryVO categoryVO;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(CategoryVO data) {
            categoryVO = data;
            tvCateName.setText(data.getName());
            ivImage.setVisibility(View.VISIBLE);
            if (data.getId().equals(Long.valueOf(-2))) {
                Glide.with(itemView.getContext())
                        .load(R.drawable.banner)
                        .into(ivImage);
            } else if (data.getId().equals(Long.valueOf(-1))) {
                Glide.with(itemView.getContext())
                        .load(R.drawable.promotion)
                        .into(ivImage);
            } else if (data.getThumbnailId() != null) {
                GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), data.getThumbnailId());
                Glide.with(itemView.getContext())
                        .asBitmap()
                        .apply(LoadImage.getOption())
                        .load(glideUrl)
                        .addListener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                e.logRootCauses("CategoryImageLoadFailed");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(ivImage);

            } else {
                ivImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            ((MainActivity) mContext).onItemClick(categoryVO);
        }
    }
}
