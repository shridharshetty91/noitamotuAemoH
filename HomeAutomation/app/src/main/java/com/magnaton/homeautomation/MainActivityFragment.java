package com.magnaton.homeautomation;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.magnaton.homeautomation.AppComponents.AppFragment;
import com.magnaton.homeautomation.AppComponents.HelperFunctions;
import com.magnaton.homeautomation.Model.DeviceInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.net.wifi.SupplicantState.COMPLETED;
import static com.android.volley.Request.Method.POST;
import static com.magnaton.homeautomation.Constants.DEBUG;
import static com.magnaton.homeautomation.Constants.Log_TAG;
import static com.magnaton.homeautomation.Constants.SharedPreferencesTag;
import static com.magnaton.homeautomation.WebcomUrls.GetDevicesUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends AppFragment {

    private View rootView;

    private ProgressBar mProgressbar;
    private ListView mListView;

    private DeviceInfo deviceData = null;
    public DeviceInfo.Device currentDevice;

    private WifiManager wifi;


    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.main_activity_fragment_ment, toolbar.getMenu());
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d(Log_TAG, "Menu clicked = " + item.getTitle());
                    return false;
                }
            });

            mProgressbar = (ProgressBar) rootView.findViewById(R.id.activity_main_progress);
            mListView = (ListView) rootView.findViewById(R.id.activity_main_list_view);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    connectToNetwork(deviceData.devices.get(position));
                }
            });

            wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            deviceData = DeviceInfo.getDeviceInfo(getContext());
            if (deviceData == null) {
                getDevices();
            } else {
                updatedValues();
            }

            //Drawer
            DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (deviceData != null && deviceData.devices.size() > 0) {
            for (DeviceInfo.Device device : deviceData.devices) {
                HelperFunctions.forgetNetwork(wifi, device.ssid);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getDevices() {

        JSONObject params = new JSONObject();
        SharedPreferences preferences = getActivity().getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
        try {
            params.put("Token", preferences.getString("Token", ""));
            params.put("uid", preferences.getString("uid", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProgress(true);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(POST, GetDevicesUrl, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Log_TAG, "Devices" + response.toString());
                showProgress(false);

                if (response != null) {
                    deviceData = new DeviceInfo(response);
                    deviceData.storeValues(getContext());
                }

                updatedValues();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showProgress(false);
            }
        });
        queue.add(request);
    }


    private void updatedValues() {
        if (deviceData != null && deviceData.devices != null && getContext() != null) {
            ArrayAdapter<DeviceInfo.Device> arrayAdapter = new ArrayAdapter<DeviceInfo.Device>(getContext(),
                    R.layout.device_info_cell, R.id.device_info_cell_textview, deviceData.devices);
            mListView.setAdapter(arrayAdapter);
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressbar.setVisibility(View.VISIBLE);
        } else {
            mProgressbar.setVisibility(View.GONE);
        }
    }

    private void showWifiInfoFragment(final DeviceInfo.Device deviceInfo) {
        WifiInfoFragment wifiInfoFragment = new WifiInfoFragment();
        wifiInfoFragment.deviceInfo = deviceInfo;
        wifiInfoFragment.listner = new WifiInfoFragment.WifiInfoFragmentListner() {
            @Override
            public void sentInformationToDeviceSuccessfully(WifiInfoFragment fragment, DeviceInfo.Device device) {
                deviceData.storeValues(getContext());
            }
        };
        addFragment(R.id.activity_main, wifiInfoFragment);
    }

    private void connectToNetwork(final DeviceInfo.Device deviceInfo) {

        currentDevice = deviceInfo;

        showProgress(true);

        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getActivity(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + deviceInfo.device_id + "\"";
        conf.preSharedKey = "\"" + deviceInfo.device_pass +"\"";
//        conf.SSID = "\"" + "Cost" + "\"";
//        conf.preSharedKey = "\"" + "zxcvb12345" +"\"";
//        conf.SSID = "\"" + "magnaton" + "\"";
//        conf.preSharedKey = "\"" + "9902340617mag" +"\"";

        HelperFunctions.forgetNetwork(wifi, deviceInfo.device_id);

        wifi.addNetwork(conf);
        //enable it
        List<WifiConfiguration> list = wifi.getConfiguredNetworks();

        if (list != null && list.size() > 0) {
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals(conf.SSID)) {
                    wifi.disconnect();
                    boolean status = wifi.enableNetwork(i.networkId, true);
                    wifi.reconnect();

                    checkIsWifiConnected(3);
                    break;
                }
            }
        }
    }

    void checkIsWifiConnected(int chances) {

        WifiInfo wifiInfo = wifi.getConnectionInfo();
        SupplicantState state = wifiInfo.getSupplicantState();
        if (state == COMPLETED) {
            //Connected

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showWifiInfoFragment(currentDevice);
                }
            }, 500);

            showProgress(false);
        } else if (chances > 0){
            chances--;
            //Wait for another 1sec
            final Handler handler = new Handler();
            final int finalChances = chances;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkIsWifiConnected(finalChances);
                }
            }, 1000);
        } else {
            //Can not connect
            HelperFunctions.forgetNetwork(wifi, currentDevice.device_id);

            new AlertDialog.Builder(getContext()).
                    setTitle("Error").
                    setMessage("Unable to connect").
                    setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            showProgress(false);
        }
    }
}
