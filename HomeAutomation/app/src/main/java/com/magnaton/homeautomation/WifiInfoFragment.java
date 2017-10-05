package com.magnaton.homeautomation;


import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.magnaton.homeautomation.AppComponents.AppFragment;
import com.magnaton.homeautomation.AppComponents.HelperFunctions;
import com.magnaton.homeautomation.Model.DeviceInfo;

import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;
import static com.magnaton.homeautomation.Constants.Log_TAG;
import static com.magnaton.homeautomation.WebcomUrls.DeviceAddressUrl;


/**
 * A simple {@link Fragment} subclass.
 */
public class WifiInfoFragment extends AppFragment {

    private WifiManager wifi;
    public DeviceInfo.Device deviceInfo;
    public WifiInfoFragmentListner listner;

    private EditText ssid;
    private EditText password;
    private EditText ipAddress;
    private EditText gateway;
    private EditText subnet;
    private Button submitButton;

    public WifiInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(Log_TAG, "WifiInfoFragment onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wifi_info, container, false);

        wifi=(WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);

        ssid = (EditText) view.findViewById(R.id.ssid);
        password = (EditText) view.findViewById(R.id.password);
        ipAddress = (EditText) view.findViewById(R.id.ip_address);
        gateway = (EditText) view.findViewById(R.id.gateway);
        subnet = (EditText) view.findViewById(R.id.subnet);
        submitButton = (Button) view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(submitButtonClickListner);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (deviceInfo != null && deviceInfo.ssid != null) {
            HelperFunctions.forgetNetwork(wifi, deviceInfo.ssid);
        }
//        HelperFunctions.forgetNetwork(wifi, "Cost");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (deviceInfo != null && deviceInfo.ssid != null) {
            HelperFunctions.forgetNetwork(wifi, deviceInfo.ssid);
        }
//        HelperFunctions.forgetNetwork(wifi, "Cost");
    }

    private View.OnClickListener submitButtonClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (validateInputs() == false) {
                new AlertDialog.Builder(getContext()).
                        setTitle("Error").
                        setMessage("Some Fields Are missing.").
                        setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return;
            }

            sendRouterInformationToDevice();
        }
    };

    private boolean validateInputs() {
        if (ssid.getText().toString().length() == 0 ||
                password.getText().toString().length() == 0 ||
                ipAddress.getText().toString().length() == 0 ||
                gateway.getText().toString().length() == 0 ||
                subnet.getText().toString().length() == 0) {
            return false;
        }

        return true;
    }
    private void sendRouterInformationToDevice() {

        String urlParams = "ssid=" + ssid.getText().toString() +
                "&pass=" + password.getText().toString() +
                "&ip="+ ipAddress.getText().toString() + "." +
                "&gateway=" + gateway.getText().toString() + "." +
                "&subnet=" + subnet.getText().toString() + ".";
        String url = DeviceAddressUrl + urlParams;

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Log_TAG, "Sent Information to Device");

                HelperFunctions.forgetNetwork(wifi, deviceInfo.device_id);

                deviceInfo.ssid = ssid.getText().toString();
                deviceInfo.password = password.getText().toString();
                deviceInfo.ipAddress = ipAddress.getText().toString() + ".";
                deviceInfo.gateway = gateway.getText().toString() + ".";
                deviceInfo.subnet = subnet.getText().toString() + ".";
                deviceInfo.isConfigured = true;
                deviceInfo.updateRawValues();

                if (listner != null) {
                    listner.sentInformationToDeviceSuccessfully(WifiInfoFragment.this, deviceInfo);
                }
                popFragment();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(Log_TAG, "Filed to Send Information to Device: " + error.toString());

                new AlertDialog.Builder(getContext()).
                        setTitle("Error").
                        setMessage("Unable to connect. Try again.").
                        setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        queue.add(request);
    }

    public static interface WifiInfoFragmentListner {
        void sentInformationToDeviceSuccessfully(WifiInfoFragment fragment, DeviceInfo.Device device);
    }
}
