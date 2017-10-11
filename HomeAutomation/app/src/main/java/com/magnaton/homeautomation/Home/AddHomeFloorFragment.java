package com.magnaton.homeautomation.Home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.magnaton.homeautomation.Constants;
import com.magnaton.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddHomeFloorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddHomeFloorFragment extends DialogFragment {

    public void setListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private boolean isPopupBeingShown = false;
    private int mSelectedIndex = -1;

    public AddHomeFloorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_home_floor, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        rootView.setMinimumWidth(width - 60);

        final EditText floorName = (EditText) rootView.findViewById(R.id.floor_name_edit_text);

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String floor = floorName.getText().toString();
                if (floor.length() > 0) {
                    if (mListener != null && mSelectedIndex != -1) {
                        mListener.floorAdded(AddHomeFloorFragment.this, floor,
                                Constants.IconTypes.values()[mSelectedIndex]);

                        dismiss();
                    }
                }
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

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
        void floorAdded(AddHomeFloorFragment sender, String floorName, Constants.IconTypes iconType);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private int[] mData = new int[]{R.mipmap.floor, R.mipmap.garden, R.mipmap.corridor,
                R.mipmap.office, R.mipmap.bedroom, R.mipmap.bathroom};
        private LayoutInflater mInflater;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView view = new ImageView(getContext());
            GridLayoutManager.LayoutParams params = new GridLayoutManager.LayoutParams(
                    GridLayoutManager.LayoutParams.WRAP_CONTENT,
                    GridLayoutManager.LayoutParams.WRAP_CONTENT
            );
            int margin = 30;
            params.setMargins(margin, margin, margin, margin);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((ImageView)holder.itemView).setImageResource(getItem(position));
            if (mSelectedIndex == position) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.app_transperent_color));
            }
        }

        // total number of cells
        @Override
        public int getItemCount() {
            return mData.length;
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedIndex = mRecyclerView.getChildAdapterPosition(v);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        }

        // convenience method for getting data at click position
        int getItem(int id) {
            return mData[id];
        }
    }

}
