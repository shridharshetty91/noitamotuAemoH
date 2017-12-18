package com.magnaton.homeautomation.Model;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.magnaton.homeautomation.AppComponents.Model.Constants;

import static com.magnaton.homeautomation.MyApp.getContext;

/**
 * Created by Shridhar on 12/3/17.
 */

public class BaseResponse {
    private boolean Status;

    public boolean getStatus() { return this.Status; }

    public void setStatus(boolean Status) { this.Status = Status; }

    private String message;

    public String getMessage() {
        if (message != null) {
            return this.message;
        }
        return "";
    }

    public void setMessage(String message) { this.message = message; }

    public boolean checkIsTokenExpired() {
        if (Constants.TokenExpireMessage.equalsIgnoreCase(getMessage())) {
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.LogoutBroadcastKey));
            return true;
        }
        return false;
    }
}
