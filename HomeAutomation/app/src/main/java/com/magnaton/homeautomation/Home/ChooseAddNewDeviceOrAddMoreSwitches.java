package com.magnaton.homeautomation.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.magnaton.homeautomation.AppComponents.Controller.RUIDialogFragment;
import com.magnaton.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseAddNewDeviceOrAddMoreSwitches.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChooseAddNewDeviceOrAddMoreSwitches extends RUIDialogFragment {

    private OnFragmentInteractionListener mListener;

    public ChooseAddNewDeviceOrAddMoreSwitches() {
        // Required empty public constructor
        throw new RuntimeException("Must use parametered Constructor");
    }

    public ChooseAddNewDeviceOrAddMoreSwitches(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_choose_add_device_or_switch_fragment_dialog,
                container, false);
        setFragmentMinWidth(rootView);

        Button addDeviceButton = (Button) rootView.findViewById(R.id.add_device_button);
        addDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseAddDevice();
                dismiss();
            }
        });

        Button addSwitchButton = (Button) rootView.findViewById(R.id.add_switch_button);
        addSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseAddMoreSwitch();
                dismiss();
            }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onChooseAddDevice() {
        if (mListener != null) {
            mListener.onChooseAddDevice();
        }
    }

    private void onChooseAddMoreSwitch() {
        if (mListener != null) {
            mListener.onChooseAddMoreSwitch();
        }
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
        void onChooseAddDevice();
        void onChooseAddMoreSwitch();
    }
}
