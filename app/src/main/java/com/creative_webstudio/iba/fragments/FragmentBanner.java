package com.creative_webstudio.iba.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductActivity;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.google.gson.Gson;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentBanner extends Fragment {
    //    @BindView(R.id.ivImage)
    ImageView ivImage;

    //    @BindView(R.id.tvDescription)
    MMTextView tvDescription;

    IBAPreferenceManager mIbaShared;

    AdvertisementVO advertisementVO;

    Context mContext;
    String description;
    Long imageId;
    String url;

    public static FragmentBanner newInstance(AdvertisementVO advertisementVO) {
        FragmentBanner fragment = new FragmentBanner();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(advertisementVO);
        args.putString("advertisement", json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mIbaShared = new IBAPreferenceManager(getContext());
        String json = getArguments().getString("advertisement");
        Gson gson = new Gson();
        advertisementVO = gson.fromJson(json, AdvertisementVO.class);
        description = advertisementVO.getDescription();
        imageId = advertisementVO.getThumbnailId();
        url = advertisementVO.getUrl();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
//        ButterKnife.bind(this,view);

        tvDescription = view.findViewById(R.id.tvDescription);
        ivImage = view.findViewById(R.id.ivImage);
//        if (tvDescription != null)
        if (description == null) {
            description = "";
        }
        tvDescription.setText(description);
        if (imageId != null) {
            GlideUrl glideUrl = LoadImage.getGlideUrl(mIbaShared.getAccessToken(), imageId);
            Glide.with(ivImage.getContext())
                    .asBitmap()
                    .apply(LoadImage.getOption())
                    .load(glideUrl)
                    .into(ivImage);
        }
        ivImage.setOnClickListener(v -> {
            ((ProductActivity) mContext).launchWebView(url);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
