package com.zhuke.svmclassifier.exceptions;

public class LoginTimeOutException extends RuntimeException {
    public LoginTimeOutException(String message) {
        super(message);
    }
}
