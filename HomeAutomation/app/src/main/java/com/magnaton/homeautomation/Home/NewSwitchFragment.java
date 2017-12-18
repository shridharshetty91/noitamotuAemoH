package com.magnaton.homeautomation.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.magnaton.homeautomation.AppComponents.Controller.RUIDialogFragment;
import com.magnaton.homeautomation.AppComponents.Views.RUIPopupMenu;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewSwitchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NewSwitchFragment extends RUIDialogFragment {

    private OnFragmentInteractionListener mListener;

    private String mName;
    private Constants.SwitchTypes mSwitchType;

    private Button mSwitchTypeButton;


    public NewSwitchFragment(String name, Constants.SwitchTypes switchType, OnFragmentInteractionListener listener) {
        mName = name;
        mSwitchType = switchType;
        mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_switch, container, false);

        setFragmentMinWidth(rootView);

        final EditText floorName = (EditText) rootView.findViewById(R.id.switch_name_edit_text);
        floorName.setText(mName);

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = floorName.getText().toString();
                if (name.length() > 0) {
                    if (mListener != null) {
                        Constants.SwitchTypes types = Constants.SwitchTypes.OnOffSwitch;
                        if (mSwitchTypeButton.getText() == "Slider") {
                            types = Constants.SwitchTypes.Slider;
                        }
                        mListener.switchAdded(name, types);
                    }

                    dismiss();
                }
            }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSwitchTypeButton = (Button) rootView.findViewById(R.id.switch_type_button);

        switch (mSwitchType) {
            case OnOffSwitch:
                mSwitchTypeButton.setText("On/Off Switch");
                break;
            case Slider:
                mSwitchTypeButton.setText("Slider");
                break;
        }

        mSwitchTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RUIPopupMenu menu = new RUIPopupMenu(getContext(), v);
                menu.getMenu().add("On/Off Switch");
                menu.getMenu().add("Slider");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mSwitchTypeButton.setText(item.getTitle());
                        return false;
                    }
                });
                menu.show();
            }
        });

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
        // TODO: Update argument type and name
        void switchAdded(String name, Constants.SwitchTypes switchType);

    }
}
