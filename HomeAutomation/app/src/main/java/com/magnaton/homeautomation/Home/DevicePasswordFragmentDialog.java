package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.magnaton.homeautomation.AppComponents.Controller.RUIDialogFragment;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.AppComponents.Views.RUIEditText;
import com.magnaton.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DevicePasswordFragmentDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DevicePasswordFragmentDialog extends RUIDialogFragment {

    private OnFragmentInteractionListener mListener;

    private RUIEditText mPasswordEditText;

    public DevicePasswordFragmentDialog() {
        throw new RuntimeException("Must use parametered Constructor");
    }

    public DevicePasswordFragmentDialog(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_password_dialog, container, false);

        try {

            setFragmentMinWidth(rootView);

            mPasswordEditText = (RUIEditText) rootView.findViewById(R.id.password_edit_text);

            Button addButton = (Button) rootView.findViewById(R.id.add_button);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String password = mPasswordEditText.getText().toString();
                    if (!TextUtils.isEmpty(password)) {
                        mListener.devicePasswordFragmentDialogPasswordAdded(DevicePasswordFragmentDialog.this, password);
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), Constants.EnterDevicePassword, Toast.LENGTH_LONG).show();
                    }
                }
            });

            Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    mListener.devicePasswordFragmentDialogOnDismiss(DevicePasswordFragmentDialog.this);
                }
            });

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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        mListener.devicePasswordFragmentDialogOnDismiss(this);
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
        void devicePasswordFragmentDialogPasswordAdded(DevicePasswordFragmentDialog sender, String password);
        void devicePasswordFragmentDialogOnDismiss(DevicePasswordFragmentDialog sender);
    }
}
