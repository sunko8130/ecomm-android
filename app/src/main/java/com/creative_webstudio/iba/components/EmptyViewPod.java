package com.creative_webstudio.iba.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.R;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aung on 11/25/17.
 */

public class EmptyViewPod extends RelativeLayout {

    @Nullable
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    @BindView(R.id.tv_empty)
    MMTextView tvEmpty;

    @BindView(R.id.btn_refresh_empty)
    MMTextView btnRefresh;

    @Nullable
    @BindView(R.id.anim_empty)
    LottieAnimationView animEmpty;

    public EmptyViewPod(Context context) {
        super(context);
    }

    public EmptyViewPod(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyViewPod(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
    }

    public void setEmptyData(int emptyImageId, String emptyMsg) {
        ivEmpty.setImageResource(emptyImageId);
        tvEmpty.setText(emptyMsg);
    }

    public void setEmptyData(String emptyMsg) {
        tvEmpty.setText(emptyMsg);
    }

    public void setRefreshButton(boolean visible) {
        if (visible) {
            btnRefresh.setVisibility(VISIBLE);
        } else {
            btnRefresh.setVisibility(GONE);
        }
    }


}
