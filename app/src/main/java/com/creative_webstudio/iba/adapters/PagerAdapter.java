package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.creative_webstudio.iba.vieholders.BaseViewHolder;

import java.util.List;

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private Context context;
    private List<AdvertisementVO> list;
    LayoutInflater mLayoutInflater;
    IBAPreferenceManager mIbaShared;

    public PagerAdapter(Context context, List<AdvertisementVO> list) {
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
        mIbaShared=new IBAPreferenceManager(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.fragment_one, container, false);
        ImageView imageView=view.findViewById(R.id.ivImage);
        if(list.get(position).getThumbnailId()!=null) {
            GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), list.get(position).getThumbnailId());
            Glide.with(imageView.getContext())
                    .asBitmap()
                    .apply(LoadImage.getOption())
                    .load(glideUrl)
                    .into(imageView);
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

//    public class PagerViewHolder extends BaseViewHolder<AdvertisementVO> {
//
//        public PagerViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void setData(AdvertisementVO data) {
//
//        }
//
//        @Override
//        public void onClick(View v) {
//
//        }
//    }
}
