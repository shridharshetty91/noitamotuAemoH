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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.magnaton.homeautomation.AppComponents.Controller.RUIFragment;
import com.magnaton.homeautomation.AppComponents.Views.RUIListView;
import com.magnaton.homeautomation.AppComponents.Views.RUITextView;
import com.magnaton.homeautomation.Constants;
import com.magnaton.homeautomation.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RoomsListFragment extends RUIFragment implements AddHomeFloorFragment.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private RUIListView mListView;
    private RoomsListFragment.ListViewAdapter mAdapter;
    private RUITextView mNoItemsTextview;

    private ArrayList<String> mFloorNames;
    private ArrayList<Constants.IconTypes> mFloorTypes;

    private String mTitle;
    private boolean mCreateNew;

    public RoomsListFragment() {

    }

    @SuppressLint("ValidFragment")
    public RoomsListFragment(String title, boolean createNew) {
        super();
        mTitle = title;
        mCreateNew = createNew;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mFloorNames = new ArrayList<>();
        mFloorTypes = new ArrayList<>();
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
                    showAddHomeFloorFragment();
                }
            });

            mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            mToolbar.setTitle(mTitle);

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

            mListView = (RUIListView) rootView.findViewById(R.id.list_view);
            mAdapter = new ListViewAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showRoomsWithSwitchesFragment(mFloorNames.get(position), false);
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
                    showAddHomeFloorFragment();
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
    public void floorAdded(AddHomeFloorFragment sender, final String floorName, Constants.IconTypes iconType) {
        mFloorNames.add(floorName);
        mFloorTypes.add(iconType);

        dataUpdated();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showRoomsWithSwitchesFragment(floorName, true);
            }
        }, Constants.DelayToPresentFragmentInMS);
    }

    private void dataUpdated() {

        if (mFloorNames.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mNoItemsTextview.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mNoItemsTextview.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void showAddHomeFloorFragment() {
        AddHomeFloorFragment floorFragment = new AddHomeFloorFragment();
        floorFragment.setListener(RoomsListFragment.this);
        floorFragment.show(getChildFragmentManager(), null);
    }

    private void showRoomsWithSwitchesFragment(String title, boolean createNew) {
        RoomWithSwitchesFragment roomsListFragment = new RoomWithSwitchesFragment(title, createNew);
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
    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFloorNames.size();
        }

        @Override
        public Object getItem(int position) {
            return mFloorNames.get(position);
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
            floorCell.setImage(mFloorTypes.get(position));
            floorCell.setFloorName(mFloorNames.get(position));

            return convertView;
        }
    }
}
