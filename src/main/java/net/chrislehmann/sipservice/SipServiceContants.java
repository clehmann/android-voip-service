package net.chrislehmann.sipservice;

public class SipServiceContants {
    public static class Actions {
        public static final String STOP_SERVICE = "net.chrislehmann.sipservice.ACTION_STOP";
        public static final String HANG_UP = "net.chrislehmann.sipservice.ACTION_HANG_UP";
        public static final String SET_SERVER_INFO = "net.chrislehmann.sipservice.ACTION_SET_SERVER_INFO";
        public static final String ENABLE_SPEAKERPHONE = "net.chrislehmann.sipservice.ACTION_ENABLE_SPEAKERPHONE";
        public static final String DISABLE_SPEAKERPHONE = "net.chrislehmann.sipservice.ACTION_DISABLE_SPEAKERPHONE";
    }

    public class Extras {
        public static final String USERNAME_KEY = "username";
        public static final String PASSWORD = "password";
        public static final String DOMAIN = "domain";
    }
}
