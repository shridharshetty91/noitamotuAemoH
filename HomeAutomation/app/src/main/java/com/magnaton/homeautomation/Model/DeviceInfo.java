package com.magnaton.homeautomation.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.magnaton.homeautomation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.magnaton.homeautomation.Constants.SharedPreferencesTag;

/**
 * Created by Shridhar on 12/3/16.
 */

public class DeviceInfo {
    public JSONObject rawValue;

    public ArrayList<Device> devices = new ArrayList<>();

    public DeviceInfo(JSONObject jsonObject) {

        rawValue = jsonObject;

        try {
            if (jsonObject == null) {
                return;
            }
            JSONArray array = jsonObject.optJSONArray("data");
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length(); i++) {
                Device device = new Device(array.optJSONObject(i));
                devices.add(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeValues(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getResources().getString(R.string.device_info), rawValue.toString());

        editor.apply();
    }

    public static DeviceInfo getDeviceInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
        String rawValue = preferences.getString(context.getResources().getString(R.string.device_info), null);
        if (rawValue != null) {
            try {
                return new DeviceInfo(new JSONObject(rawValue));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    static public class Device {
        public String device_id = null;
        public String device_pass = null;
        public String ssid = null;
        public String password= null;
        public String ipAddress = null;
        public String gateway = null;
        public String subnet = null;
        public Boolean isConfigured = false;

        public JSONObject rawValue;

        public Device(JSONObject jsonObject) {

            rawValue = jsonObject;

            if (jsonObject == null) {
                return;
            }

            try {
                device_id = jsonObject.optString("Device_id", null);
                device_pass = jsonObject.optString("Device_pass", null);
                ssid = jsonObject.optString("ssid", null);
                password = jsonObject.optString("password", null);
                ipAddress = jsonObject.optString("ipAddress", null);
                gateway = jsonObject.optString("gateway", null);
                subnet = jsonObject.optString("subnet", null);
                isConfigured = jsonObject.optBoolean("isConfigured", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void updateRawValues() {
            try {
                rawValue.put("Device_id", device_id);
                rawValue.put("Device_pass", device_pass);
                rawValue.put("ssid", ssid);
                rawValue.put("password", password);
                rawValue.put("ipAddress", ipAddress);
                rawValue.put("gateway", gateway);
                rawValue.put("subnet", subnet);
                rawValue.put("isConfigured", isConfigured);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return device_id;
        }
    }

    static public class RouterInformation {
        public String ssid = null;
        public String password = null;
        public String gateway = null;
        public String subnet = null;

        static RouterInformation sharedRouterInfo = new RouterInformation();
    }
}

