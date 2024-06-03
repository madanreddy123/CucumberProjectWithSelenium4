package com.Cucumber.automation.framework.constants;

import com.Cucumber.automation.framework.utils.PropertyReader;

public class AppConstants {

    public static class Web {
        public static String UI_BASE_URL = null;
        public static String GRID_HUB_URL = null;
        public static String GRIP_HUB_PORT = null;

        static {
            try {
                UI_BASE_URL = PropertyReader.getFieldValue("UI_BASE_URL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                GRIP_HUB_PORT = PropertyReader.getFieldValue("GRID_HUB_PORT");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                GRID_HUB_URL = "http://" + PropertyReader.getFieldValue("GRID_HUB_IP") + ":" +
                        GRIP_HUB_PORT + "/wd/hub";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class Api {
        public static String API_BASE_URL = null;
        public static String API_USERNAME = null;
        public static String API_PASSWORD = null;
        public static String API_GRANT_TYPE = null;
        public static String API_TOKEN = null;
        public static String API_SCOPE = null;
        public static String API_ACCESS_TOKEN_URL = null;
        public static String API_CLIENT_ID = null;
        public static String API_CLIENT_SECRET = null;
        public static String API_REDIRECT_URL = null;
        public static String API_RESPONSE_TYPE = null;
        public static String API_AUTHORIZATION_CODE = null;
        public static String API_CODE_CHALLENGE = null;
        public static String API_CODE_CHALLENGE_METHOD = null;

        public enum AuthenticationType {
            Basic,
            Digest,
            Token,
            OAuth2
        }

        public enum GrantType {
            Authorization_Code,
            Client_Credentials,
            Password
        }

        static {
            try {
                API_BASE_URL = PropertyReader.getFieldValue("API_BASE_URL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_USERNAME = PropertyReader.getFieldValue("API_USERNAME");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_PASSWORD = PropertyReader.getFieldValue("API_PASSWORD");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_GRANT_TYPE = PropertyReader.getFieldValue("API_GRANT_TYPE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_TOKEN = PropertyReader.getFieldValue("API_TOKEN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_SCOPE = PropertyReader.getFieldValue("API_SCOPE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_ACCESS_TOKEN_URL = PropertyReader.getFieldValue("API_ACCESS_TOKEN_URL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_CLIENT_ID = PropertyReader.getFieldValue("API_CLIENT_ID");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_CLIENT_SECRET = PropertyReader.getFieldValue("API_CLIENT_SECRET");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_REDIRECT_URL = PropertyReader.getFieldValue("API_REDIRECT_URL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_RESPONSE_TYPE = PropertyReader.getFieldValue("API_RESPONSE_TYPE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_AUTHORIZATION_CODE = PropertyReader.getFieldValue("API_AUTHORIZATION_CODE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_CODE_CHALLENGE = PropertyReader.getFieldValue("API_CODE_CHALLENGE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static {
            try {
                API_CODE_CHALLENGE_METHOD = PropertyReader.getFieldValue("API_CODE_CHALLENGE_METHOD");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
