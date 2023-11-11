package org.example;

public enum TimeServletConstants {
    DEFAULT_CONTENT_TYPE("text/html"),
    COOKIE_LAST_TIMEZONE_NAME("lastTimezone"),
    PARAMETER_NAME("timezone");
    private final String text;
    TimeServletConstants(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
