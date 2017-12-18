package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.magnaton.homeautomation.AppComponents.Controller.RUIDialogFragment;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.AppComponents.Views.RUIEditText;
import com.magnaton.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFloorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFloorFragment extends RUIDialogFragment {

    private RUIEditText mFloorNameEditText;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    private OnFragmentInteractionListener mListener;
    private boolean isPopupBeingShown = false;

    private boolean mEdit;
    private String mFloorName;
    private int mSelectedIndex = -1;
    private Object mUserInfo;

    public AddFloorFragment() {
    }

    public AddFloorFragment(OnFragmentInteractionListener listener, String name, int iconIndex, boolean edit, Object userInfo) {
        mListener = listener;
        mEdit = edit;
        mFloorName = name;
        mSelectedIndex = iconIndex;
        mUserInfo = userInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_floor, container, false);
        setFragmentMinWidth(rootView);

        mFloorNameEditText = (RUIEditText) rootView.findViewById(R.id.floor_name_edit_text);
        mFloorNameEditText.setText(mFloorName);

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        if (mEdit) {
            addButton.setText(getString(R.string.update_button_title));
        } else {
            addButton.setText(getString(R.string.add_button_title));
        }
        addButton.setOnClickListener(mOnClickAddButton);

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

    private View.OnClickListener mOnClickAddButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String floor = mFloorNameEditText.getText().toString();
                if (floor.length() > 0 && mSelectedIndex != -1) {
                    mListener.floorAdded(AddFloorFragment.this, floor,
                            Constants.IconTypes.fromInteger(mSelectedIndex + 1), mEdit, mUserInfo);

                    dismiss();
                } else {
                    Toast.makeText(getContext(), getString(R.string.enter_floor_name), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), getString(R.string.enter_floor_name), Toast.LENGTH_LONG).show();
            }
        }
    };

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
        void floorAdded(AddFloorFragment sender, String floorName, Constants.IconTypes iconType, boolean edit, Object userInfo);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private int[] mData = new int[]{R.mipmap.floor, R.mipmap.garden, R.mipmap.corridor,
                R.mipmap.office, R.mipmap.bedroom, R.mipmap.bathroom};
        private LayoutInflater mInflater;

        // getData is passed into the constructor
        MyRecyclerViewAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView view = new ImageView(getContext());
            int padding = 5;
            view.setPadding(padding, padding, padding, padding);
            GridLayoutManager.LayoutParams params = new GridLayoutManager.LayoutParams(
                    GridLayoutManager.LayoutParams.WRAP_CONTENT,
                    GridLayoutManager.LayoutParams.WRAP_CONTENT
            );
            int margin = 30;
            params.setMargins(margin, margin, margin, margin);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        // binds the getData to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((ImageView)holder.itemView).setImageResource(getItem(position));
            if (mSelectedIndex == position) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.app_grey2_color));
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

        // convenience method for getting getData at click position
        int getItem(int id) {
            return mData[id];
        }
    }

}
