package com.appagility.shipping;

public class NotReadyForSailingException extends IllegalStateException {

    public NotReadyForSailingException() {

        super("A ship cannot be sailed that is not ready for sailing");
    }
}
