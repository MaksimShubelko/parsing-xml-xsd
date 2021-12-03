package com.java.by.shubelko.parsing.exception;

public class MedProductException extends Exception {

    public MedProductException() {
        super();
    }

    public MedProductException(String message) {
        super(message);
    }

    public MedProductException(Exception e) {
        super(e);
    }

    public MedProductException(String message, Exception e) {
        super(message, e);
    }
}
