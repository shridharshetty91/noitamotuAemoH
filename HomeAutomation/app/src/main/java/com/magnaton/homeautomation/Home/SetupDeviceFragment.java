package com.magnaton.homeautomation.Home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magnaton.homeautomation.AppComponents.Controller.RUIDialogFragment;
import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.AppComponents.Model.HelperFunctions;
import com.magnaton.homeautomation.AppComponents.Views.RUIButton;
import com.magnaton.homeautomation.AppComponents.Views.RUIEditText;
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.WebcomUrls;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetupDeviceFragment extends RUIDialogFragment {

    private ISetupDeviceFragment mListner;
    private String mDeviceId;
    private String mParent_id;
    private Stage_2 mRoomData;

    private Toolbar mToolbar;

    private RUIEditText mSsidEditText;
    private RUIEditText mPasswordEditText;

    private RUIButton mConnectButton;
    private RUIButton mCancelButton;

    @SuppressLint("ValidFragment")
    public SetupDeviceFragment(ISetupDeviceFragment listner, String deviceId, String parent_id, Stage_2 roomData) {
        mListner = listner;
        mDeviceId = deviceId;
        mParent_id = parent_id;
        mRoomData = roomData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_setup_device, container, false);
        setFragmentMinWidth(rootView);
//        InitialSetupForRootView(rootView);

        try {

            mSsidEditText = (RUIEditText) rootView.findViewById(R.id.ssid_edit_text);
            mPasswordEditText = (RUIEditText) rootView.findViewById(R.id.password_edit_text);

            mConnectButton = (RUIButton) rootView.findViewById(R.id.connect_button);
            mConnectButton.setOnClickListener(mClickedOnConnectButton);

            mCancelButton = (RUIButton) rootView.findViewById(R.id.cancel_button);
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    mListner.setupDeviceFragmentOnDismiss(SetupDeviceFragment.this);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener mClickedOnConnectButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String ssid = mSsidEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password)) {

                    Toast.makeText(getContext(), "All Fields are mandatory", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, String> params = new HashMap<>();
                    AppPreference appPreference = AppPreference.getAppPreference();
                    params.put(Constants.UserIdKey, appPreference.getUserId());
                    params.put(Constants.UserTokenKey, appPreference.getUserToken());
                    params.put(Constants.DeviceIdKey, mDeviceId);
                    params.put(Constants.ParentIdKey, mParent_id);
                    params.put(Constants.ChildIdKey, mRoomData.getChild_id());
                    params.put(Constants.HomeSsidKey, ssid);
                    params.put(Constants.HomePasswordKey, password);

                    CallSetUpDeviceAPI(params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void CallSetUpDeviceAPI(final Map<String, String> params) {

        HelperFunctions.ShowProgressDialog(getContext());

        final StringRequest request = new StringRequest(Request.Method.POST,
                WebcomUrls.SetUpDevice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HelperFunctions.DismissProgressDialog();

                        Toast.makeText(getContext(), Constants.DeviceConfigured, Toast.LENGTH_LONG).show();
                        dismiss();

                        mListner.setupDeviceFragmentDeviceConfigured(SetupDeviceFragment.this, mDeviceId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        HelperFunctions.DismissProgressDialog();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    interface ISetupDeviceFragment {
        void setupDeviceFragmentDeviceConfigured(SetupDeviceFragment sender, String deviceId);
        void setupDeviceFragmentOnDismiss(SetupDeviceFragment sender);
    }
}
