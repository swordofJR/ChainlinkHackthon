package com.cuit.alias.common.exception;

/**
 * @author lisihan
 * @version 1.0
 **/
public class AppException extends RuntimeException{
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
