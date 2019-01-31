package com.creative_webstudio.iba.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductActivity;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.google.gson.Gson;


public class FragmentDetailBanner extends Fragment {

    ImageView ivImage;
    IBAPreferenceManager mIbaShared;
    Context mContext;
    Long imageId;
    public static FragmentDetailBanner newInstance(Long imageId) {
        FragmentDetailBanner fragment = new FragmentDetailBanner();
        Bundle args = new Bundle();
        args.putLong("imageId", imageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mIbaShared = new IBAPreferenceManager(getContext());
        this.imageId = getArguments().getLong("imageId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_banner, container, false);
        ivImage = view.findViewById(R.id.ivImage);
        if (imageId != 0) {
            GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), imageId);
            Glide.with(ivImage.getContext())
                    .asBitmap()
                    .apply(LoadImage.getOption())
                    .load(glideUrl)
                    .into(ivImage);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
