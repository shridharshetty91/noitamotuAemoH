package com.magnaton.homeautomation.Dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.magnaton.homeautomation.Account.LoginActivity;
import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.Home.HomeFragment;
import com.magnaton.homeautomation.Profile.ProfileFragment;
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.Settings.SettingsFragment;

public class DashboardActivity extends AppCompatActivity
        implements LeftNavigationView.ILeftNavigationView, OnDashboardActivityFragmentInteractionListener {

    private Constants.SideMenuOptions _currentSelectedOption = Constants.SideMenuOptions.None;
    private Fragment mCurrentFragment;

    private Toolbar toolbar;
    private LeftNavigationView mLeftNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addObservers();

        mLeftNavigationView = (LeftNavigationView) findViewById(R.id.left_navigation_view);
        mLeftNavigationView.delegate = this;

        setCurrentSelectedOption(Constants.SideMenuOptions.Home);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObservers();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            int count =  fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                _currentSelectedOption = Constants.SideMenuOptions.Home;
                mLeftNavigationView.setCurrentSelectedOption(_currentSelectedOption);
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Dashboard/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clickedOn(LeftNavigationView sender, Constants.SideMenuOptions option) {

        setCurrentSelectedOption(option);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setCurrentSelectedOption(Constants.SideMenuOptions option) {
        if (option == Constants.SideMenuOptions.Logout) {
            return;
        }

        if (_currentSelectedOption != option) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (mCurrentFragment != null &&
                    _currentSelectedOption != Constants.SideMenuOptions.None &&
                    _currentSelectedOption != Constants.SideMenuOptions.Home) {
                transaction.remove(mCurrentFragment);
                fragmentManager.popBackStackImmediate();
                transaction.commitNow();
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
            }

            _currentSelectedOption = option;

            switch (_currentSelectedOption) {
                case Home: {
                    if (mCurrentFragment == null) {
                        mCurrentFragment = HomeFragment.newInstance();
                        transaction.add(R.id.dashboard_root_view, mCurrentFragment)
                                .commit();
                    }
                    break;
                }
                case Settings: {
                    mCurrentFragment = SettingsFragment.newInstance();
                    transaction.replace(R.id.dashboard_root_view, mCurrentFragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    break;
                }
                case Profile: {
                    mCurrentFragment = ProfileFragment.newInstance();
                    transaction.replace(R.id.dashboard_root_view, mCurrentFragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    break;
                }
                case Logout: {
                    break;
                }
            }

            mLeftNavigationView.setCurrentSelectedOption(_currentSelectedOption);
        }
    }

    @Override
    public void setToolBar(Toolbar toolBar) {
        toolbar = toolBar;

        setSupportActionBar(toolbar);
        toolbar.bringToFront();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void addObservers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(logoutReceiver, new IntentFilter(Constants.LogoutBroadcastKey));
    }

    private BroadcastReceiver logoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppPreference.getAppPreference().setLoginData(null);
            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    };

    private void removeObservers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutReceiver);
    }
}

