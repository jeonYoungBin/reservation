package com.reservation.reservation.exception;

public class CustomExcepiton extends RuntimeException {
    public CustomExcepiton() {
    }
    public CustomExcepiton(String message) {
        super(message);
    }
    public CustomExcepiton(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomExcepiton(Throwable cause) {
        super(cause);
    }
}
