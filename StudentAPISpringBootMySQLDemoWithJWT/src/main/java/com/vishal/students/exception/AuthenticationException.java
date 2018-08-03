package com.vishal.students.exception;
@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    public AuthenticationException(String message) {
    	super(message);
    }
}
