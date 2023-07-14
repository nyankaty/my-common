package com.company.common.util;

import java.util.List;

public final class Constant {

    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ANONYMOUS_ACCOUNT = "anonymous";
    public static final String AUTHORITY_TYPE = "auth_type";
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String CLIENT = "client";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CLIENT_AUTHORITY = "client";
    public static final String CLIENT_MESSAGE_ID = "client_message_id";
    public static final String REMOTE_IP = "remote_ip";
    public static final String API_DOC = "custom_api_doc";
    public static final String EXCEPTION_MESSAGE = "custom_exception_message";

    public static final String DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String PHONE_REGEX = "(84+([0-9]{9})\\b)";
    public static final String CHECK_NUMBER_REGEX = "[0-9]+";
    public static final String LANGUAGE_VI = "vi";
    public static final String LANGUAGE_EN = "en";

    public static final List<String> EXTENSIONS = List.of("bmp", "jpg", "png", "jpeg");

}
