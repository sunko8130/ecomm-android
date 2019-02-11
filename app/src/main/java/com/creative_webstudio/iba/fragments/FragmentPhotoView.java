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
import com.creative_webstudio.iba.activities.ProductDetailsActivity;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;


public class FragmentPhotoView extends Fragment {

    ImageView ivImage;
    IBAPreferenceManager mIbaShared;
    Context mContext;
    Long imageId;
    public static FragmentPhotoView newInstance(Long imageId) {
        FragmentPhotoView fragment = new FragmentPhotoView();
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
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);
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
