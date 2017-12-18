package com.magnaton.homeautomation.Home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.AppComponents.Model.HelperFunctions;
import com.magnaton.homeautomation.AppComponents.Views.RUIPopupMenu;
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.WebcomUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.android.volley.Request.Method.POST;
import static com.magnaton.homeautomation.AppComponents.Model.Constants.Log_TAG;
import static com.magnaton.homeautomation.AppComponents.Model.Constants.SharedPreferencesTag;

/**
 * TODO: document your custom view class.
 */
public class SwitchCell extends RelativeLayout {

    public SwitchCellListner listner;

    private ImageView mImageView_more;
    private TextView mTitleTextView;
    private Switch mSwitch;
    private SeekBar mSeekbar;

    private String mName;
    private Constants.SwitchTypes mSwitchType;

    public SwitchCell(Context context) {
        super(context);
        init(null, 0);
    }

    public SwitchCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SwitchCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Update TextPaint and text measurements from attributes
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initializeSwitchCell();
    }

    private void initializeSwitchCell() {
        mImageView_more = (ImageView) findViewById(R.id.imageView_more);
        mTitleTextView = (TextView) findViewById(R.id.title_textview);
        mSwitch = (Switch) findViewById(R.id.switch_view);
        mSeekbar = (SeekBar) findViewById(R.id.seek_bar);


        mImageView_more.setOnClickListener(mOnClickMoreImage);

        mSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendSwitchValues();
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                sendSwitchValues();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setTitle(String title) {
        mName = title;
        mTitleTextView.setText(mName);
    }

    public void setSwitchType(Constants.SwitchTypes switchType) {
        mSwitchType = switchType;
        switch (mSwitchType) {
            case OnOffSwitch:
                mSwitch.setVisibility(VISIBLE);
                mSeekbar.setVisibility(GONE);
                break;
            case Slider:
                mSwitch.setVisibility(GONE);
                mSeekbar.setVisibility(VISIBLE);
                break;
        }
    }

    private void sendSwitchValues() {

        try {
            HelperFunctions.ShowProgressDialog(getContext());

            RequestQueue queue = Volley.newRequestQueue(getContext());

            JSONObject params = new JSONObject();

            params.put("buttons", "00101");



            JsonObjectRequest request = new JsonObjectRequest(POST, WebcomUrls.DeviceAddressUrl, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(Log_TAG, response.toString());
                    HelperFunctions.DismissProgressDialog();

                    if (response.optString("CODE").equals("501")) {
                    } else {
                        SharedPreferences preferences = getContext().getSharedPreferences(SharedPreferencesTag, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("Token", response.optString("Token"));
//                        editor.putString("uid", response.optString("uid"));
                        editor.apply();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    HelperFunctions.DismissProgressDialog();
                }
            });

            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            HelperFunctions.DismissProgressDialog();
        }
    }

    public interface SwitchCellListner {
        void EditSwitch(SwitchCell sender, String name, Constants.SwitchTypes switchType);
    }

    private OnClickListener mOnClickMoreImage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            RUIPopupMenu popupMenu = new RUIPopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.switch_cell_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.switch_cell_menu_schedule:

                            final Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH);
                            int day = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            final Calendar c = Calendar.getInstance();
                                            int mHour = c.get(Calendar.HOUR_OF_DAY);
                                            int mMinute = c.get(Calendar.MINUTE);
                                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                                    new TimePickerDialog.OnTimeSetListener() {

                                                        @Override
                                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                                              int minute) {
                                                        }
                                                    }, mHour, mMinute, true);
                                            timePickerDialog.show();

                                        }
                                    }, year, month, day);
                            datePickerDialog.show();
                            break;
                        case R.id.switch_cell_menu_edit:
                            if (listner != null) {
                                listner.EditSwitch(SwitchCell.this, mName, mSwitchType);
                            }
                            break;
                        case R.id.switch_cell_menu_delete:
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    };
}
