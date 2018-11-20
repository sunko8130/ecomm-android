package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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

import com.creative_webstudio.iba.BaseActivity;
import com.creative_webstudio.iba.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle = null;
    private AlertDialog dialog;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.flDrawerChildLayout)
    FrameLayout flDrawerChildLayout;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_drawer);
        ButterKnife.bind(this, this);
        addDrawerItems();
        setupDrawer();

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);
//        navigationView.setNavigationItemSelectedListener(this);
        context = this;
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

    private void addDrawerItems() {
        if (this instanceof ProductActivity) {
            navigationView.getMenu().findItem(R.id.menuProduct).setChecked(true);
        } else if (this instanceof CartActivity) {
            navigationView.getMenu().findItem(R.id.menuCart).setChecked(true);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.menuProduct):
                        if (item.getItemId() == R.id.menuProduct && !(context instanceof ProductActivity)) {
                            startActivity(ProductActivity.newIntent(context));
//                        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                        }
                        break;
                    case (R.id.menuCart):
                        if (item.getItemId() == R.id.menuCart && !(context instanceof CartActivity)) {
                            startActivity(CartActivity.newIntent(context));
//                        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                        }
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
        if (this instanceof CartActivity) {
            startActivity(ProductActivity.newIntent(this));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (this instanceof ProductActivity) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                moveTaskToBack(true);
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    doubleBackToExitPressedOnce = false;
//                    handler.postDelayed(this, 2000);
                }
            }, 2000);
        }
    }

    private void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menuProduct):
                break;
            case (R.id.menuCart):
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
