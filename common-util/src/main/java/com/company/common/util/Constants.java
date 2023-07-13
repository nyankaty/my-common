package com.company.common.util;

import java.util.List;

public final class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BASE_PACKAGES = "com.company.common";
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

    public static final List<String> EXTENSIONS = List.of("bmp", "jpg", "png", "jpeg");

}
