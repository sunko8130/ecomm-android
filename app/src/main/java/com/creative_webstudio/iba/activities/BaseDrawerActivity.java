package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.datas.vos.LogOutVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.mmtextview.components.MMTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseDrawerActivity extends BaseActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.flDrawerChildLayout)
    FrameLayout flDrawerChildLayout;

    private Context context;

    private CustomerVO customerVO;


    IBAPreferenceManager ibaShared;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_drawer);
        ButterKnife.bind(this, this);
        ibaShared = new IBAPreferenceManager(this);
//        getCustomerInfo();
        addDrawerItems();
        setupDrawer();
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
//        navigationView.setNavigationItemSelectedListener(this);
        context = this;
//        navigationView.getHeaderView(R.id.tvCustomerName);
    }


    private void setupCustomerInfo() {
        customerVO = IbaModel.getInstance().getCustomerVO();
        View headerView = navigationView.getHeaderView(0);
        MMTextView name = headerView.findViewById(R.id.tvCustomerName);
        MMTextView email = headerView.findViewById(R.id.tvCustomerEmail);
        if (customerVO != null) {
            if (customerVO.getName() != null)
                name.setText(customerVO.getName());
            if (customerVO.getEmail() != null)
                email.setText(customerVO.getEmail());
        }
        headerView.setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            startActivity(ProfileActivity.newIntent(context));
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setMyView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutResID, null);
        flDrawerChildLayout.addView(view);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.save, R.string.product) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCustomerInfo();
        navigationView.getMenu().findItem(R.id.menuProduct).setChecked(true);
    }

    private void addDrawerItems() {
        if (this instanceof MainActivity) {
            navigationView.getMenu().findItem(R.id.menuProduct).setChecked(true);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case (R.id.menuProduct):
                    if (item.getItemId() == R.id.menuProduct && !(context instanceof MainActivity)) {
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_products");
                        mFirebaseAnalytics.logEvent("click_product", bundle);
                        startActivity(MainActivity.newIntent(context));
//                            finish();
//                        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                    }
                    break;
                case (R.id.menuCart):
                    if (item.getItemId() == R.id.menuCart && !(context instanceof CartActivity)) {
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_cart");
                        mFirebaseAnalytics.logEvent("click_cart", bundle);
                        startActivity(CartActivity.newIntent(context));
//                            finish();
//                        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                    }
                    break;
                case (R.id.orderHistory):
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_order_history");
                    mFirebaseAnalytics.logEvent("click_order_history", bundle);
                    startActivity(new Intent(context, OrderHistoryActivity.class));
                    break;

                case (R.id.menuLogout):
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Logout!");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        logOut();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    AlertDialog productDialog = builder.create();
                    productDialog.setCanceledOnTouchOutside(false);
                    productDialog.show();

                    break;

            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void logOut() {
        Bundle bundle = new Bundle();
        CustomerVO customerVO = IbaModel.getInstance().getCustomerVO();
        bundle.putLong("customer_id", customerVO.getId());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "logout");
        mFirebaseAnalytics.logEvent("click_log_out", bundle);
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                logOut();
            });
        } else {
            IbaModel.getInstance().userLogOut().observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    ibaShared.clear();
                    startActivity(SignInActivity.newIntent(this));
                    finish();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            // Invalid or expired access token.
                            super.refreshAccessToken();
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("Connection Error!");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                logOut();
                            });
                        }
                    }
                }
            });


        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                moveTaskToBack(true);
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                //Do something after 100ms
                doubleBackToExitPressedOnce = false;
//                    handler.postDelayed(this, 2000);
            }, 2000);
        }
    }


}
