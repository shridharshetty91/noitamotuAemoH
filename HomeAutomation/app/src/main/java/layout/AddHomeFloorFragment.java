package layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.magnaton.homeautomation.AppComponents.Views.RUIPopupMenu;
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

    private OnFragmentInteractionListener mListener;
    private boolean isPopupBeingShown = false;

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

        final EditText floorType = (EditText) rootView.findViewById(R.id.floor_type_edit_text);
        floorType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (isPopupBeingShown == false) {
                    isPopupBeingShown = true;
                    RUIPopupMenu popup = new RUIPopupMenu(getContext(), v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.home_floor_types, popup.getMenu());

                    popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            isPopupBeingShown = false;
                        }
                    });

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            floorType.setText(item.getTitle());
                            floorType.setCompoundDrawablesRelative(item.getIcon(), null, null, null);
                            return false;
                        }
                    });
                    popup.show();
                }
                return true;
            }
        });
        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String floor = floorName.getText().toString();
                if (floor.length() > 0) {
                    if (mListener != null) {
                        mListener.floorAdded(AddHomeFloorFragment.this, floor, floorType.getText().toString());

                        dismiss();
                    }
                }
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
        void floorAdded(AddHomeFloorFragment sender, String floorName, String floorType);
    }
}
