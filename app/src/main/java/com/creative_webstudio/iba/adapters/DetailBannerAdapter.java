package com.creative_webstudio.iba.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creative_webstudio.iba.fragments.FragmentDetailBanner;

import java.util.List;

public class DetailBannerAdapter extends FragmentPagerAdapter {
    private List<Long> imageList;
    public DetailBannerAdapter(FragmentManager fm,List<Long> imageList) {
        super(fm);
        this.imageList=imageList;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentDetailBanner.newInstance(imageList.get(position));
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
