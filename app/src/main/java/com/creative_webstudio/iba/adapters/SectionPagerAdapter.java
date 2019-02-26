package com.creative_webstudio.iba.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.fragments.FragmentBanner;

import java.util.ArrayList;
import java.util.List;


public class SectionPagerAdapter extends FragmentPagerAdapter {

    private List<AdvertisementVO> adList;

    public SectionPagerAdapter(FragmentManager fm,List<AdvertisementVO> adList) {
        super(fm);
        this.adList=adList;
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentBanner.newInstance(adList.get(i));
        }

    @Override
    public int getCount() {
        return adList.size();
    }

}
