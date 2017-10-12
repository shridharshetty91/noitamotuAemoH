package com.magnaton.homeautomation.AppComponents.Model;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Shridhar on 12/28/16.
 */

public class HelperFunctions {
    private static ProgressDialog progress;

    public static void forgetNetwork(WifiManager wifi, String ssid) {
        List<WifiConfiguration> list = wifi.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                wifi.disconnect();
                wifi.disableNetwork(i.networkId);
                wifi.removeNetwork(i.networkId);
            }
        }
    }

    public static void ShowProgressDialog(Context context) {
        ShowProgressDialog(context, "Requesting", "Please wait…");
    }

    public static void ShowProgressDialog(Context context, String title, String message) {
        DismissProgressDialog();
        progress = new ProgressDialog(context);
        progress.setTitle(title);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        progress.setCancelable(false);
    }

    public static void DismissProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }
}
