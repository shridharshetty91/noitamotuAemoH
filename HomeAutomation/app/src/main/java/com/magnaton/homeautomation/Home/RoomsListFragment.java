package com.magnaton.homeautomation.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.WebcomUrls;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RoomsListFragment extends RUIFragment implements AddFloorFragment.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private RUIListView mListView;
    private RoomsListFragment.ListViewAdapter mAdapter;
    private RUITextView mNoItemsTextview;

    private Stage_1 mFloorData;
    private boolean mCreateNew;

    public RoomsListFragment() {

    }

    @SuppressLint("ValidFragment")
    public RoomsListFragment(Stage_1 floorData, boolean createNew) {
        super();
        mFloorData = floorData;
        mCreateNew = createNew;
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
        View rootView = inflater.inflate(R.layout.fragment_rooms_list, container, false);
        InitialSetupForRootView(rootView);

        try {
            ImageView fab = (ImageView) rootView.findViewById(R.id.plus_imageview);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddFloorFragment(null, 0, false, null);
                }
            });

            mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            mToolbar.setTitle(mFloorData.getName());

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

            mListView = (RUIListView) rootView.findViewById(R.id.list_view);
            mAdapter = new ListViewAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showRoomsWithSwitchesFragment(mFloorData.getStage_2().get(position), false);
                }
            });
            mNoItemsTextview = (RUITextView) rootView.findViewById(R.id.no_items_label);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        dataUpdated();

        if (mCreateNew) {
            mCreateNew = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showAddFloorFragment(null, 0, false, null);
                }
            }, Constants.DelayToPresentFragmentInMS);
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
            CallAddFloorFragmentAPI(floorName, iconType, true, (Stage_2) userInfo);
        } else {
            CallAddFloorFragmentAPI(floorName, iconType, false, null);
        }
    }

    private void CallAddFloorFragmentAPI(final String floorName, final Constants.IconTypes iconType, final boolean edit, final Stage_2 oldStage2) {

        HelperFunctions.ShowProgressDialog(getContext());

        final StringRequest request = new StringRequest(Request.Method.POST,
                WebcomUrls.Stage2Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleAddFloorAPISucess(response, edit, oldStage2);
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
                    params.put(Constants.ChildIdKey, oldStage2.getChild_id());
                } else {
                    params.put(Constants.FlagKey, String.valueOf(1)); //Edit
                }

                params.put(Constants.ParentIdKey, mFloorData.getParent_id());
                params.put(Constants.ChildNameKey, floorName);
                params.put(Constants.ChildIconKey, String.valueOf(iconType.getValue()));

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void handleAddFloorAPISucess(String response, boolean edit, final Stage_2 oldStage2) {
        try {

            Stage2Response stage2Response = new Gson().fromJson(response, Stage2Response.class);

            if (stage2Response != null) {
                if (!stage2Response.checkIsTokenExpired() && stage2Response.getStatus() && stage2Response.getData() != null) {
                    Stage_2 updateStage_2;
                    if (edit) {
                        updateStage_2 = oldStage2;
                    } else {
                        updateStage_2 = new Stage_2();
                        mFloorData.getStage_2().add(updateStage_2);
                    }
                    updateStage_2.setName(stage2Response.getData().getChild_name());
                    updateStage_2.setChild_icon(stage2Response.getData().getChild_icon());
                    updateStage_2.setChild_id(stage2Response.getData().getChild_id());
                    DashboardResponse.valueUpdated();

                    dataUpdated();
                } else {
                    Toast.makeText(getContext(), stage2Response.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void handleError(VolleyError error) {
        error.printStackTrace();
        Log.d(Constants.Log_TAG, error.getMessage());

        Toast.makeText(getContext(), Constants.ServerFailed, Toast.LENGTH_SHORT).show();
        HelperFunctions.DismissProgressDialog();
    }

    private void dataUpdated() {

        if (mFloorData.getStage_2() != null && mFloorData.getStage_2().size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mNoItemsTextview.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mNoItemsTextview.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showAddFloorFragment(String name, int iconIndex, boolean edit, Stage_2 oldData) {
        AddFloorFragment floorFragment = new AddFloorFragment(this, name, iconIndex, edit, oldData);
        floorFragment.show(getFragmentManager(), null);
    }

    private void showRoomsWithSwitchesFragment(Stage_2 stage2Response, boolean createNew) {
        RoomWithSwitchesFragment roomsListFragment = new RoomWithSwitchesFragment(mFloorData.getParent_id(), stage2Response, createNew);
        addFragment(R.id.rootView, roomsListFragment);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /*
    * Adapter
    * */
    class ListViewAdapter extends BaseAdapter implements FloorCell.IFloorCell {

        @Override
        public int getCount() {
            return mFloorData.getStage_2().size();
        }

        @Override
        public Object getItem(int position) {
            return mFloorData.getStage_2().get(position);
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
            Stage_2 stage_2 = mFloorData.getStage_2().get(position);
            floorCell.setData(stage_2);
            floorCell.setListner(this);
            floorCell.setImage(Constants.IconTypes.fromInteger(stage_2.getChild_icon()));
            floorCell.setFloorName(stage_2.getName());

            return convertView;
        }

        @Override
        public void floorCellEdit(FloorCell sender, Object data) {
            Stage_2 cellData = (Stage_2) data;
            showAddFloorFragment(cellData.getName(), cellData.getChild_icon() - 1, true, cellData);
        }

        @Override
        public void floorCellDelete(FloorCell sender, Object data) {

        }
    }
}
