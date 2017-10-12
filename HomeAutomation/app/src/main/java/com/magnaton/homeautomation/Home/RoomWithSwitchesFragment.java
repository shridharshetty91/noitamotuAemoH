package com.magnaton.homeautomation.Home;


import android.annotation.SuppressLint;
import android.content.Context;
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
 */
public class RoomWithSwitchesFragment extends RUIFragment {

    private Toolbar mToolbar;
    private RUIListView mListView;
    private RoomWithSwitchesFragment.ListViewAdapter mAdapter;
    private RUITextView mNoItemsTextview;

    private ArrayList<String> mFloorNames;
    private ArrayList<Constants.SwitchTypes> mSwitchTypes;

    private String mTitle;
    private boolean mCreateNew;

    public RoomWithSwitchesFragment() {

    }

    @SuppressLint("ValidFragment")
    public RoomWithSwitchesFragment(String title, boolean createNew) {
        super();
        mTitle = title;
        mCreateNew = createNew;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mFloorNames = new ArrayList<>();
        mFloorNames.add("Tube light 1 ");
        mFloorNames.add("Fan 1");
        mFloorNames.add("Tube light 2");

        mSwitchTypes = new ArrayList<>();
        mSwitchTypes.add(Constants.SwitchTypes.OnOffSwitch);
        mSwitchTypes.add(Constants.SwitchTypes.Slider);
        mSwitchTypes.add(Constants.SwitchTypes.OnOffSwitch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_room_with_switches, container, false);
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
            mAdapter = new RoomWithSwitchesFragment.ListViewAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.switch_cell, parent, false);
            }

            SwitchCell floorCell = (SwitchCell) convertView;
            floorCell.setTitle(mFloorNames.get(position));
            floorCell.setSwitchType(mSwitchTypes.get(position));

            return convertView;
        }
    }
}
