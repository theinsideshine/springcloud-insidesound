package com.theinsideshine.insidesound.backend.exceptions;

public enum InsidesoundErrorCode {

    NOT_EMPTY_HOTEL_ID(400,"Hotel ID cannot be null or empty."),

    INVALID_CHECK_OUT(400,"The checkout date must be later than the check-in date."),

    NOT_EMPTY_CHECK_OUT(400,"The check-out date cannot be null or empty."),

    NOT_EMPTY_CHECK_IN(400,"The check-in date cannot be null or empty."),

    CHECK_IN_EARLIER_TODAY(400,"The check-in date cannot be earlier than today."),

    CHECK_OUT_EARLIER_TODAY(400,"The check-out date cannot be earlier than today."),

    INVALID_AGE (400, "Age must be greater than 0."),

    EMPTY_AGES (400, "Ages list cannot be empty."),

    INVALID_SEARCH_ID(400, "No search found with the provided Id."),

    ERROR_SENDING_KAFKA(500, "Error sending message to Kafka");

    private final int code;
    private final String reason;

    InsidesoundErrorCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
