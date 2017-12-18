package com.magnaton.homeautomation.AppComponents.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.magnaton.homeautomation.Account.LoginResponse;
import com.magnaton.homeautomation.Home.DashboardResponse;
import com.magnaton.homeautomation.MyApp;

import static com.magnaton.homeautomation.AppComponents.Model.Constants.SharedPreferencesTag;

/**
 * Created by Shridhar on 11/11/17.
 */

public class AppPreference {

    private static AppPreference sharedPreference = null;
    public static AppPreference getAppPreference() {
        if (sharedPreference == null) {
            sharedPreference = new AppPreference();
        }

        return sharedPreference;
    }

    private SharedPreferences getSharedPreferences() {
        return MyApp.getContext().getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
    }

    private String LoginDataKey = "LoginDataKey";
    private LoginResponse.Data loginData;

    public LoginResponse.Data getLoginData() {
        try {
            if (loginData == null) {
                SharedPreferences preferences = getSharedPreferences();
                String rawValue = preferences.getString(LoginDataKey, null);
                if (rawValue != null) {
                    loginData = new Gson().fromJson(rawValue, LoginResponse.Data.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginData;
    }

    public void setLoginData(LoginResponse.Data loginData) {
        this.loginData = loginData;

        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        if (loginData != null) {
            editor.putString(LoginDataKey, new Gson().toJson(loginData));
        } else {
            editor.putString(LoginDataKey, null);
        }
        editor.apply();
    }

    public String getUserToken() {
        LoginResponse.Data loginData = getLoginData();
        if (loginData != null) {
            return loginData.getUserToken();
        }

        return null;
    }

    public String getUserId() {
        LoginResponse.Data loginData = getLoginData();
        if (loginData != null) {
            return loginData.getUserId();
        }

        return null;
    }

    public String getUserFirstName() {
        LoginResponse.Data loginData = getLoginData();
        if (loginData != null) {
            return loginData.getUserFirstName();
        }

        return null;
    }

    private String HomeAutomationDataKey = "HomeAutomationDataKey";
    private DashboardResponse mHomeAutomationData;
    public DashboardResponse getHomeAutomationData() {
        if (mHomeAutomationData == null) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            String rawValue = sharedPreferences.getString(HomeAutomationDataKey, null);
            if (rawValue != null) {
                mHomeAutomationData = new Gson().fromJson(rawValue, DashboardResponse.class);
            } else {
                setHomeAutomationData(new DashboardResponse());
            }
        }

        return mHomeAutomationData;
    }

    public void setHomeAutomationData(DashboardResponse newValue) {
        mHomeAutomationData = newValue;

        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (mHomeAutomationData != null) {
            editor.putString(HomeAutomationDataKey, new Gson().toJson(mHomeAutomationData));
        } else {
            editor.putString(HomeAutomationDataKey, null);
        }

        editor.apply();
    }
}
