package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.magnaton.homeautomation.AppComponents.Controller.RUIFragment;
import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.AppComponents.Model.HelperFunctions;
import com.magnaton.homeautomation.AppComponents.Views.RUIListView;
import com.magnaton.homeautomation.AppComponents.Views.RUITextView;
import com.magnaton.homeautomation.Dashboard.OnDashboardActivityFragmentInteractionListener;
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.WebcomUrls;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDashboardActivityFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends RUIFragment implements AddFloorFragment.OnFragmentInteractionListener {

    private OnDashboardActivityFragmentInteractionListener mListener;
    private Toolbar mToolbar;
    private RUIListView mListView;
    private HomeFragmentListViewAdapter mAdapter;
    private RUITextView mNoItemsTextview;

    private DashboardResponse mData;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mData = DashboardResponse.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        InitialSetupForRootView(rootView);

        ImageView fab = (ImageView) rootView.findViewById(R.id.plus_imageview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFloorFragment(null, 0, false, null);
            }
        });

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mListener != null) {
            mListener.setToolBar(mToolbar);
        }

        mListView = (RUIListView) rootView.findViewById(R.id.list_view);
        mAdapter = new HomeFragmentListViewAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRoomsListFragment(position);
            }
        });

        mNoItemsTextview = (RUITextView) rootView.findViewById(R.id.no_items_label);

        TextView welcomeTextView = (TextView) rootView.findViewById(R.id.welcome_message_textview);
        welcomeTextView.setText("Welcome, " + AppPreference.getAppPreference().getUserFirstName());

        GetDashboardData();

        return rootView;
    }

    @Override
    protected void InitialSetupForRootView(View rootView) {
        rootView.setClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        dataUpdated();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mToolbar.setTitle("Home");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDashboardActivityFragmentInteractionListener) {
            mListener = (OnDashboardActivityFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void floorAdded(AddFloorFragment sender, String floorName, Constants.IconTypes iconType, boolean edit, Object userInfo) {
        if (edit) {
            CallAddFloorFragmentAPI(floorName, iconType, true, (Stage_1) userInfo);
        } else {
            CallAddFloorFragmentAPI(floorName, iconType, false, null);
        }
    }

    private void handleError(VolleyError error) {
        error.printStackTrace();
        Log.d(Constants.Log_TAG, error.getMessage());

        Toast.makeText(getContext(), Constants.ServerFailed, Toast.LENGTH_SHORT).show();
        HelperFunctions.DismissProgressDialog();
    }

    private void dataUpdated() {

        if (mData != null && mData.getStage_1().size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mNoItemsTextview.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mNoItemsTextview.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showRoomsListFragment(int position) {
        Stage_1 stage_1 = mData.getStage_1().get(position);
        RoomsListFragment roomsListFragment = new RoomsListFragment(stage_1, false);
        addFragment(R.id.rootView, roomsListFragment);
    }

    private class HomeFragmentListViewAdapter extends BaseAdapter implements FloorCell.IFloorCell {

        @Override
        public int getCount() {
            if (mData != null && mData.getStage_1() != null) {
                return mData.getStage_1().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.getStage_1().get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.floor_cell, parent, false);
            }

            FloorCell floorCell = (FloorCell) convertView;
            floorCell.setListner(this);
            Stage_1 stage_1 = mData.getStage_1().get(position);
            floorCell.setData(stage_1);
            floorCell.setImage(Constants.IconTypes.fromInteger(stage_1.getParent_icon()));
            floorCell.setFloorName(stage_1.getName());

            return convertView;
        }

        @Override
        public void floorCellEdit(FloorCell sender, Object data) {
            Stage_1 cellData = (Stage_1) data;
            showAddFloorFragment(cellData.getName(), cellData.getParent_icon() - 1, true, cellData);
        }

        @Override
        public void floorCellDelete(FloorCell sender, Object data) {

        }
    }

    private void GetDashboardData() {

        HelperFunctions.ShowProgressDialog(getContext());

        final StringRequest request = new StringRequest(Request.Method.POST,
                WebcomUrls.DashboardDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleDashboardAPISucess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                AppPreference appPreference = AppPreference.getAppPreference();
                params.put(Constants.UserIdKey, appPreference.getUserId());
                params.put(Constants.UserTokenKey, appPreference.getUserToken());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void handleDashboardAPISucess(String response) {
        try {

            DashboardResponse dashboardResponse = new Gson().fromJson(response, DashboardResponse.class);

            if (dashboardResponse != null) {
                if (!dashboardResponse.checkIsTokenExpired() && dashboardResponse.getStatus()) {
                    mData = dashboardResponse;
                    DashboardResponse.setData(mData);
                    dataUpdated();
                } else {
                    Toast.makeText(getContext(), dashboardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), Constants.ServerFailed, Toast.LENGTH_SHORT).show();
            }

            HelperFunctions.DismissProgressDialog();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallAddFloorFragmentAPI(final String floorName, final Constants.IconTypes iconType, final boolean edit, final Stage_1 oldStage1) {

        HelperFunctions.ShowProgressDialog(getContext());

        final StringRequest request = new StringRequest(Request.Method.POST,
                WebcomUrls.Stage1Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleAddFloorAPISucess(response, edit, oldStage1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                AppPreference appPreference = AppPreference.getAppPreference();
                params.put(Constants.UserIdKey, appPreference.getUserId());
                params.put(Constants.UserTokenKey, appPreference.getUserToken());
                if (edit) {
                    params.put(Constants.FlagKey, String.valueOf(2)); //Update
                    params.put(Constants.ParentIdKey, oldStage1.getParent_id());
                } else {
                    params.put(Constants.FlagKey, String.valueOf(1)); //Edit
                }

                params.put(Constants.ParentNameKey, floorName);
                params.put(Constants.ParentIconKey, String.valueOf(iconType.getValue()));

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void handleAddFloorAPISucess(String response, boolean edit, final Stage_1 oldStage1) {
        try {

            Stage1Response stage1Response = new Gson().fromJson(response, Stage1Response.class);

            if (stage1Response != null) {
                if (!stage1Response.checkIsTokenExpired() && stage1Response.getStatus() && stage1Response.getData() != null) {
                    Stage_1 updateStage_1;
                    if (edit) {
                        updateStage_1 = oldStage1;
                    } else {
                        updateStage_1 = new Stage_1();
                        mData.getStage_1().add(updateStage_1);
                    }
                    updateStage_1.setName(stage1Response.getData().getParent_name());
                    updateStage_1.setParent_icon(stage1Response.getData().getParent_icon());
                    updateStage_1.setParent_id(stage1Response.getData().getParent_id());
                    DashboardResponse.valueUpdated();

                    dataUpdated();
                } else {
                    Toast.makeText(getContext(), stage1Response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), Constants.ServerFailed, Toast.LENGTH_SHORT).show();
            }

            HelperFunctions.DismissProgressDialog();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddFloorFragment(String name, int iconIndex, boolean edit, Stage_1 oldData) {
        AddFloorFragment floorFragment = new AddFloorFragment(this, name, iconIndex, edit, oldData);
        floorFragment.show(getFragmentManager(), null);
    }
}
