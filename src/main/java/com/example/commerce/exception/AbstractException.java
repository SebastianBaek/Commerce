package com.example.commerce.exception;

public abstract class AbstractException extends RuntimeException {

  abstract public int getStatusCode();

  abstract public String getMessage();
}
