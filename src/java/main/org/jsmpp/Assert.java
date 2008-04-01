package org.jsmpp;

public class Assert {
    public static void isNotNull(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void notNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value can't be null");
        }
    }

    public static void isNull(Object value) {
        if (value != null) {
            throw new IllegalStateException("Value is not null: " + value);
        }

    }
}
