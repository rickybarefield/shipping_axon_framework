package com.appagility.shipping;

public class AlreadyReadyForSailingException extends IllegalStateException {

    public AlreadyReadyForSailingException() {

        super("A ship cannot be made ready for sailing when it's already ready");
    }
}
