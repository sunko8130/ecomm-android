package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProfielEditViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.etCurrentPw)
    TextInputEditText etCurrentPw;

    @BindView(R.id.etNewPw)
    TextInputEditText etNewPw;

    @BindView(R.id.etReTypePw)
    TextInputEditText etReTypePw;

    @BindView(R.id.tilNewPw)
    TextInputLayout tilNewPw;

    @BindView(R.id.tilRetypePw)
    TextInputLayout tilRetypePw;

    @BindView(R.id.btnOk)
    Button btnOk;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    //loading dialog
    AlertDialog loadingDialog;

    private ProfielEditViewModel editViewModel;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this, this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        editViewModel = ViewModelProviders.of(this).get(ProfielEditViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (etCurrentPw.getText().toString().trim().equalsIgnoreCase("")) {
                    etCurrentPw.setError("Enter Current Password");
                } else if (etNewPw.getText().toString().trim().equalsIgnoreCase("")) {
                    tilNewPw.setError("Enter new Password");
                    tilRetypePw.setError(null);
                } else if (etNewPw.getText().toString().length()<8) {
                    tilNewPw.setError("Your password must have at least 8 characters");
                    tilRetypePw.setError(null);
                } else if (etReTypePw.getText().toString().trim().equalsIgnoreCase("")) {
                    tilRetypePw.setError("Re-type your new password");
                    tilNewPw.setError(null);
                } else if (!etNewPw.getText().toString().trim().equalsIgnoreCase(etReTypePw.getText().toString())) {
                    tilRetypePw.setError("Retype password doesn't match");
                    tilNewPw.setError(null);
                } else {
                    tilNewPw.setError(null);
                    tilRetypePw.setError(null);
                    updatePassword();
                }
                break;

            case R.id.btnCancel:
                super.onBackPressed();
                break;
        }
    }

    private void updatePassword() {
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Updating Password.Please wait!");
        loadingDialog.show();
        editViewModel.updatePassword(etCurrentPw.getText().toString(), etNewPw.getText().toString()).observe(this, apiResponse -> {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (apiResponse.getData() != null) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                builder.setTitle("Success");
                builder.setMessage("Your password update is success!");
                builder.setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    super.onBackPressed();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 400) {
                        retryDialog.show();
                        retryDialog.btnRetry.setText("Ok");
                        retryDialog.tvRetry.setText("Your current password is incorrect.Try Again!");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                        });
                    }
                } else {
                    retryDialog.show();
                    retryDialog.tvRetry.setText("Network error!");
                    retryDialog.btnRetry.setOnClickListener(v -> {
                        retryDialog.dismiss();
                        updatePassword();
                    });
                }
            }
        });
    }
}
