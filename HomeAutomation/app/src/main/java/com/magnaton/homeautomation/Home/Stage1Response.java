package com.magnaton.homeautomation.Home;

import com.magnaton.homeautomation.Model.BaseResponse;

/**
 * Created by Shridhar on 12/12/17.
 */

public class Stage1Response extends BaseResponse {

    private Data data;

    public Data getData ()
    {
        return data;
    }

    public void setData (Data data)
    {
        this.data = data;
    }

    public class Data
    {
        private String parent_name;

        private String user_id;

        private String parent_icon;

        private String parent_id;

        public String getParent_name ()
        {
            return parent_name;
        }

        public void setParent_name (String parent_name)
        {
            this.parent_name = parent_name;
        }

        public String getUser_id ()
        {
            return user_id;
        }

        public void setUser_id (String user_id)
        {
            this.user_id = user_id;
        }

        public String getParent_icon ()
        {
            return parent_icon;
        }

        public void setParent_icon (String parent_icon)
        {
            this.parent_icon = parent_icon;
        }

        public String getParent_id ()
        {
            return parent_id;
        }

        public void setParent_id (String parent_id)
        {
            this.parent_id = parent_id;
        }
    }
}