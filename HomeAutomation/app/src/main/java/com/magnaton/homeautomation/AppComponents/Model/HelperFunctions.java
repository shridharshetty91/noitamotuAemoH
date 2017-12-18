package com.magnaton.homeautomation.AppComponents.Model;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


    public static Map<String, String> toMap(String string) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject object = new JSONObject(string);
            Iterator<String> keysItr = object.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap(value.toString());
                }
                map.put(key, value.toString());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        try {
            for(int i = 0; i < array.length(); i++) {
                Object value = array.get(i);
                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }

                else if(value instanceof JSONObject) {
                    value = toMap(value.toString());
                }
                list.add(value);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
