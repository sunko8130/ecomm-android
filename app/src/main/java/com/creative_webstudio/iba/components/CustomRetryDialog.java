package com.creative_webstudio.iba.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.creative_webstudio.iba.R;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomRetryDialog extends Dialog {

    @BindView(R.id.btnRetry)
    public
    Button btnRetry;

    @BindView(R.id.tvRetry)
    public
    MMTextView tvRetry;

    public Context context;
    public CustomRetryDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_retry);
        ButterKnife.bind(this,this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}
