package com.creative_webstudio.iba.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creative_webstudio.iba.fragments.FragmentDetailBanner;
import com.creative_webstudio.iba.fragments.FragmentPhotoView;

import java.util.List;

public class DetailBannerAdapter extends FragmentPagerAdapter {
    private List<Long> imageList;
    private int type;
    public DetailBannerAdapter(FragmentManager fm,List<Long> imageList,int Type) {
        super(fm);
        this.imageList=imageList;
        this.type = Type;
    }

    @Override
    public Fragment getItem(int position) {
        if(type==1){
            return FragmentDetailBanner.newInstance(imageList.get(position));
        }else {
            return FragmentPhotoView.newInstance(imageList.get(position));
        }
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
