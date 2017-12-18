package com.magnaton.homeautomation.Account;

/**
 * Created by Shridhar on 11/11/17.
 */

public class LoginResponse
{
    private boolean Status;

    public boolean getStatus() { return this.Status; }

    public void setStatus(boolean Status) { this.Status = Status; }

    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    private Data data;

    public Data getData() { return this.data; }

    public void setData(Data data) { this.data = data; }

    public class Data
    {
        private String user_id;

        public String getUserId() { return this.user_id; }

        public void setUserId(String user_id) { this.user_id = user_id; }

        private String user_first_name;

        public String getUserFirstName() { return this.user_first_name; }

        public void setUserFirstName(String user_first_name) { this.user_first_name = user_first_name; }

        private String user_last_name;

        public String getUserLastName() { return this.user_last_name; }

        public void setUserLastName(String user_last_name) { this.user_last_name = user_last_name; }

        private String user_email;

        public String getUserEmail() { return this.user_email; }

        public void setUserEmail(String user_email) { this.user_email = user_email; }

        private String user_mobile;

        public String getUserMobile() { return this.user_mobile; }

        public void setUserMobile(String user_mobile) { this.user_mobile = user_mobile; }

        private String user_token;

        public String getUserToken() { return this.user_token; }

        public void setUserToken(String user_token) { this.user_token = user_token; }
    }
}