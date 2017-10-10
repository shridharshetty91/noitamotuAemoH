package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.magnaton.homeautomation.AppComponents.AppFragment;
import com.magnaton.homeautomation.Dashboard.OnDashboardActivityFragmentInteractionListener;
import com.magnaton.homeautomation.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDashboardActivityFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends AppFragment implements AddHomeFloorFragment.OnFragmentInteractionListener {

    private OnDashboardActivityFragmentInteractionListener mListener;
    private Toolbar mToolbar;
    private ListView mListView;
    private ArrayList<String> mFloorNames;
    private ArrayList<String> mFloorTypes;

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

        mFloorNames = new ArrayList<>();
        mFloorTypes = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView fab = (ImageView) rootView.findViewById(R.id.plus_imageview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHomeFloorFragment floorFragment = new AddHomeFloorFragment();
                floorFragment.setListener(HomeFragment.this);
                floorFragment.show(getChildFragmentManager(), null);
            }
        });

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (mListener != null) {
            mListener.setToolBar(mToolbar);
        }

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mListView.setAdapter(new HomeFragmentListViewAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addFragment(R.id.rootView, new RoomsListFragment());
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeFragmentListViewAdapter)mListView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

//        menu.add("Home Page");

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
    public void floorAdded(AddHomeFloorFragment sender, String floorName, String floorType) {
        mFloorNames.add(floorName);
        mFloorTypes.add(floorType);

        ((HomeFragmentListViewAdapter)mListView.getAdapter()).notifyDataSetChanged();
    }

    class HomeFragmentListViewAdapter extends BaseAdapter {

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
