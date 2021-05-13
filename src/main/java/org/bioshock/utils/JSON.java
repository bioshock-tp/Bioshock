package org.bioshock.utils;

public abstract class JSON {
    /**
     * Used for JSON keys
     */
    public static final String NAME = "Name";

    /**
     * Used for JSON keys
     */
    public static final String PASSWORD = "Password";

    /**
     * Used for JSON keys
     */
    public static final String TOKEN = "Token";

    /**
     * Used for JSON keys
     */
    public static final String SCORE = "Score";

    /**
     * Used for JSON keys
     */
    public static final String MESSAGE = "Message";

    /**
     * Used for JSON keys
     */
    public static final String STATUS = "Status";

    /**
     * A string used for setting request headers
     */
    public static final String JSON_HEADER = "application/json; utf-8";

    /**
     * A string used for setting request headers
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * A string used for setting request headers
     */
    public static final String GET = "GET";

    /**
     * A string used for setting request headers
     */
    public static final String PUT = "PUT";

    /**
     * A string used for setting request headers
     */
    public static final String POST = "POST";

    /**
     * A string used when the scoring server address is incorrect
     */
    public static final String ADDRESS_INCORRECT =
        "Scoring server address incorrect";

    /**
     * A generic string used when an error occurs
     */
    public static final String AN_ERROR_OCCURRED = "An error occurred";

    private JSON() {}
}
