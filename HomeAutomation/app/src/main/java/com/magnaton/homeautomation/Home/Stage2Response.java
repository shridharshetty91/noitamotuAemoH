package com.magnaton.homeautomation.Home;

import com.magnaton.homeautomation.Model.BaseResponse;

/**
 * Created by Shridhar on 12/12/17.
 */

public class Stage2Response extends BaseResponse {

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
        private String child_name;

        private String child_icon;

        private String child_id;

        private String user_id;

        private String parent_id;

        public String getChild_name ()
        {
            return child_name;
        }

        public void setChild_name (String child_name)
        {
            this.child_name = child_name;
        }

        public int getChild_icon_in_integer()
        {
            return Integer.parseInt(child_icon);
        }

        public String getChild_icon() {
            return child_icon;
        }

        public void setChild_icon (String child_icon)
        {
            this.child_icon = child_icon;
        }

        public String getChild_id ()
        {
            return child_id;
        }

        public void setChild_id (String child_id)
        {
            this.child_id = child_id;
        }

        public String getUser_id ()
        {
            return user_id;
        }

        public void setUser_id (String user_id)
        {
            this.user_id = user_id;
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
