package com.appagility.shipping.command;

public class NotReadyForSailingException extends IllegalStateException {

    public NotReadyForSailingException() {

        super("A ship cannot be sailed that is not ready for sailing");
    }
}
