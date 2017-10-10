package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.magnaton.homeautomation.AppComponents.AppFragment;
import com.magnaton.homeautomation.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RoomsListFragment extends AppFragment implements AddHomeFloorFragment.OnFragmentInteractionListener {

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private ListView mListView;
    private ArrayList<String> mFloorNames;
    private ArrayList<String> mFloorTypes;

    public RoomsListFragment() {
        // Required empty public constructor
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

        try {
            ImageView fab = (ImageView) rootView.findViewById(R.id.plus_imageview);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddHomeFloorFragment floorFragment = new AddHomeFloorFragment();
                    floorFragment.setListener(RoomsListFragment.this);
                    floorFragment.show(getChildFragmentManager(), null);
                }
            });

            mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

            mListView = (ListView) rootView.findViewById(R.id.list_view);
            mListView.setAdapter(new ListViewAdapter());
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
    public void floorAdded(AddHomeFloorFragment sender, String floorName, String floorType) {
        mFloorNames.add(floorName);
        mFloorTypes.add(floorType);

        ((ListViewAdapter)mListView.getAdapter()).notifyDataSetChanged();
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
