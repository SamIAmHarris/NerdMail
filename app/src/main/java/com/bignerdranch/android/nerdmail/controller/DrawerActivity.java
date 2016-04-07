package com.bignerdranch.android.nerdmail.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.nerdmail.R;
import com.bignerdranch.android.nerdmail.model.EmailNotifier;

public class DrawerActivity extends AppCompatActivity {
    private static final String TAG = "DrawerActivity";
    private static final String EXTRA_CURRENT_DRAWER_ITEM
            = "DrawerActivity.CurrentDrawerItem";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int currentToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = (Toolbar) findViewById(R.id.activity_drawer_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.activity_drawer_navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameView = (TextView) headerView.findViewById(R.id.nav_drawer_header_username);
        usernameView.setText("nerdynerd@bignerdranch.com");
        navigationView.setNavigationItemSelectedListener(item -> {
            //le switch le fragments
            drawerLayout.closeDrawers();

            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_drawer_inbox:
                    fragment = new InboxFragment();
                    currentToolbarTitle = R.string.nav_drawer_inbox;
                    break;
                case R.id.nav_drawer_important:
                    fragment = new ImportantFragment();
                    currentToolbarTitle = R.string.nav_drawer_important;
                    break;
                case R.id.nav_drawer_spam:
                    fragment = new SpamFragment();
                    currentToolbarTitle = R.string.nav_drawer_spam;
                    break;
                case R.id.nav_drawer_all:
                    fragment = new AllFragment();
                    currentToolbarTitle = R.string.nav_drawer_all;
                    break;
                default:
                    Log.e(TAG, "Incorrect nav drawer item selection");
                    return false;

            }
            updateToolbarTitle();
            updateFragment(fragment);
            return true;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open_drawer_content_description,
                R.string.close_drawer_content_description);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            updateFragment(new InboxFragment());
            currentToolbarTitle = R.string.nav_drawer_inbox;
        } else {
            currentToolbarTitle = savedInstanceState.getInt(
                    EXTRA_CURRENT_DRAWER_ITEM, R.string.nav_drawer_inbox);
        }
    }

    private void updateToolbarTitle() {
        if(currentToolbarTitle != 0) {
            toolbar.setTitle(currentToolbarTitle);
        }
    }

    private void updateFragment(Fragment fragment) {
        if(fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.activity_drawer_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbarTitle();
        markEmailsAsNotified();
        clearNotifications();
    }

    private void markEmailsAsNotified() {
        Intent intent = EmailService.getClearIntent(this);
        startService(intent);
    }

    private void clearNotifications() {
        EmailNotifier notifier = EmailNotifier.get(this);
        notifier.clearNotifications();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CURRENT_DRAWER_ITEM, currentToolbarTitle);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
