package com.pliesveld.flashnote.schema;

import javax.persistence.GenerationType;

/**
 * Single source for parameters used in schema generation
 */
public class Constants {
    public static final GenerationType ID_GENERATOR = GenerationType.AUTO;

    public static final int MIN_STUDENT_NAME_LENGTH = 3;
    public static final int MAX_STUDENT_NAME_LENGTH = 32;

    public static final int MIN_STUDENT_EMAIL_LENGTH = 5;
    public static final int MAX_STUDENT_EMAIL_LENGTH = 48;

    public static final int MIN_STUDENT_PASSWORD_LENGTH = 1;
    public static final int MAX_STUDENT_PASSWORD_LENGTH = 60;

    public static final int MAX_STATEMENT_CONTENT_LENGTH = 65600;

    public static final int MAX_ATTACHMENT_TEXT_FILE_LENGTH = 65600;
    public static final int MAX_ATTACHMENT_BINARY_FILE_LENGTH = 3145728;
    public static final int MAX_ATTACHMENT_FILENAME_LENGTH = 48;

    public static final int MAX_CATEGORY_NAME_LENGTH = 34;
    public static final int MAX_CATEGORY_DESCRIPTION_LENGTH = 512;

    public static final int MAX_DECK_TITLE_LENGTH = 177;

    public static final int MAX_ACCOUNT_TOKEN_LENGTH = 64;

    public static final long PASSWORD_RESET_TOKEN_DURATION_DAYS = 7;
    public static final long REGISTRATION_TOKEN_DURATION_DAYS = 7;


    public static int ACCESS_LIMIT_LOGIN_DELAY = 30;
}
