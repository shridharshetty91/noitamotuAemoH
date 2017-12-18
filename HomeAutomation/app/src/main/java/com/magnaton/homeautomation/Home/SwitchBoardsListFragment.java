package com.magnaton.homeautomation.Home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.magnaton.homeautomation.AppComponents.Controller.RUIFragment;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwitchBoardsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SwitchBoardsListFragment extends RUIFragment implements DevicePasswordFragmentDialog.OnFragmentInteractionListener, SetupDeviceFragment.ISetupDeviceFragment {

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private ListView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private HomeFragmentListViewAdapter mAdapter;

    private String wifis[];
    private List<ScanResult> wifiScanList;
    private int mSelectedIndex = -1;

    private String mParent_id;
    private Stage_2 mRoomData;

    public SwitchBoardsListFragment() {
        // Required empty public constructor
        throw new RuntimeException("Must use parametered Constructor");
    }

    public SwitchBoardsListFragment(OnFragmentInteractionListener listener, String parent_id, Stage_2 roomData) {
        mListener = listener;
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
        View rootView = inflater.inflate(R.layout.fragment_switch_board_list, container, false);

        try {
            InitialSetupForRootView(rootView);

            mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            mToolbar.setTitle("Available Switchboards");

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

            mListView = (ListView) rootView.findViewById(R.id.list_view);
            mAdapter = new HomeFragmentListViewAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedIndex = position;
                    mAdapter.notifyDataSetChanged();

                    DevicePasswordFragmentDialog dialog = new DevicePasswordFragmentDialog(SwitchBoardsListFragment.this);
                    dialog.show(getChildFragmentManager(), null);
                }
            });
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchWifiList();
                }
            });

            fetchWifiList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void InitialSetupForRootView(View rootView) {
        rootView.setClickable(true);
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

    @Override
    public void devicePasswordFragmentDialogPasswordAdded(DevicePasswordFragmentDialog sender, String password) {
        connectToDevice(wifis[mSelectedIndex], password);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiManager wifiManager = getWifiManager();
                String connectedSSID = wifiManager.getConnectionInfo().getSSID();
                String refe = "\"" + wifis[mSelectedIndex] + "\"";
                if (refe.equals(connectedSSID)) {
                    SetupDeviceFragment fragment = new SetupDeviceFragment(SwitchBoardsListFragment.this, wifis[mSelectedIndex], mParent_id, mRoomData);
                    fragment.show(getChildFragmentManager(), null);
                } else {
                    Toast.makeText(getContext(), "Looks like you have entered wrong password", Toast.LENGTH_LONG).show();
                }
            }
        }, 2000);
    }

    @Override
    public void devicePasswordFragmentDialogOnDismiss(DevicePasswordFragmentDialog sender) {
        mSelectedIndex = -1;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setupDeviceFragmentDeviceConfigured(SetupDeviceFragment sender, String deviceId) {
        resetData();
    }

    @Override
    public void setupDeviceFragmentOnDismiss(SetupDeviceFragment sender) {
        resetData();
    }

    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            fetchWifiList();
        }
    }

    private WifiManager getWifiManager() {
        return (WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void resetData() {
        mSelectedIndex = -1;
        fetchWifiList();
    }

    private void fetchWifiList() {
        mSwipeRefreshLayout.setRefreshing(true);

        final WifiManager wifiManager = getWifiManager();
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getContext(), "Wifi is disabled. So turning on", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);

            //Need delay to get turn on wifi.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (wifiManager.isWifiEnabled()) {
                        fetchWifiList();
                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 2000);

            return;
        }

        if (requestLocationPermission()) {
            wifiScanList = wifiManager.getScanResults();

            wifis = new String[wifiScanList.size()];

            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = (wifiScanList.get(i)).SSID;
            }

            mAdapter.notifyDataSetChanged();
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private boolean requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);

            return false;
        }

        return true;
    }

    private void connectToDevice(String ssid, String networkPass) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
//            conf.wepTxKeyIndex = 0;

        //for WEP network
//        conf.wepKeys[0] = "\"" + networkPass + "\"";
//        conf.wepTxKeyIndex = 0;
//        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        //For WPA network
        conf.preSharedKey = "\""+ networkPass +"\"";

//        //For Open network
//        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager wifiManager = getWifiManager();
        wifiManager.addNetwork(conf);

        //enable it
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        if (list != null) {
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                    wifiManager.disconnect();
                    boolean status = wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();

                    break;
                }
            }
        }
    }

    private class HomeFragmentListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (wifis != null) {
                return wifis.length;
            }

            return 0;
        }

        @Override
        public Object getItem(int position) {
            return wifis[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.switch_board_cell, parent, false);
            }

            SwitchBoardCell switchBoardCell = (SwitchBoardCell) convertView;
            switchBoardCell.setDeviceName(wifis[position]);
            if (mSelectedIndex == position) {
                switchBoardCell.setImageSelected(true);
            } else {
                switchBoardCell.setImageSelected(false);
            }
            return convertView;
        }
    }
}
