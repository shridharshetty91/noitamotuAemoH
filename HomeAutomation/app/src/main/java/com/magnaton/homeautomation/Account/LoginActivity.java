package com.magnaton.homeautomation.Account;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.magnaton.homeautomation.AppComponents.Model.HelperFunctions;
import com.magnaton.homeautomation.BuildConfig;
import com.magnaton.homeautomation.AppComponents.Model.Constants;
import com.magnaton.homeautomation.Dashboard.DashboardActivity;
import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.R;
import com.magnaton.homeautomation.WebcomUrls;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email_edit_text);

        mPasswordView = (EditText) findViewById(R.id.password_edit_text);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        if (BuildConfig.DEBUG) {
            mEmailView.setText("shridhar.shetty91@gmail.com");
            mPasswordView.setText("password");
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, //set a gradient direction
                new int[] {0xFFFFFFFF}); //set the color of gradient
        gradientDrawable.setCornerRadius(10f);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            sendLoginRequet(email, password);
        }
    }

    private void sendLoginRequet(final String email, final String password) {
        HelperFunctions.ShowProgressDialog(this);

        final StringRequest request = new StringRequest(Request.Method.POST,
                WebcomUrls.LoginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleLoginSucess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleLoginError(error);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email", email.trim());
                params.put("user_password", password.trim());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void handleLoginSucess(String response) {
        try {

            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);

            if (loginResponse != null) {
                if (loginResponse.getStatus()) {
                    AppPreference.getAppPreference().setLoginData(loginResponse.getData());

                    StartDashboardActivity();
                } else {
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, Constants.ServerFailed, Toast.LENGTH_SHORT).show();
            }

            HelperFunctions.DismissProgressDialog();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginError(VolleyError error) {
        error.printStackTrace();
        Log.d(Constants.Log_TAG, error.getMessage());

        Toast.makeText(LoginActivity.this, Constants.ServerFailed, Toast.LENGTH_SHORT).show();
        HelperFunctions.DismissProgressDialog();
    }

    private void StartDashboardActivity()
    {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);

        finish();
    }

    private boolean isEmailValid(String email) {

        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }
}

