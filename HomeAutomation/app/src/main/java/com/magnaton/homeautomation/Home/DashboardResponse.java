package com.magnaton.homeautomation.Home;

import com.magnaton.homeautomation.AppComponents.Model.AppPreference;
import com.magnaton.homeautomation.Model.BaseResponse;

import java.util.ArrayList;

/**
 * Created by Shridhar on 11/12/17.
 */

public class DashboardResponse extends BaseResponse {

    private ArrayList<Stage_1> Stage_1;

    public DashboardResponse () {
        Stage_1 = new ArrayList<>();
    }

    public ArrayList<Stage_1> getStage_1 ()
    {
        return Stage_1;
    }

    public void setStage_1 (ArrayList<Stage_1> Stage_1)
    {
        this.Stage_1 = Stage_1;
    }


    private static DashboardResponse mHomeAutomationData;

    public static DashboardResponse getData() {
        mHomeAutomationData = AppPreference.getAppPreference().getHomeAutomationData();
        return mHomeAutomationData;
    }

    public static void setData(DashboardResponse response) {
        mHomeAutomationData = response;
        valueUpdated();
    }

    public static void valueUpdated() {
        AppPreference.getAppPreference().setHomeAutomationData(mHomeAutomationData);
    }
}

