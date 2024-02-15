package com.appagility.shipping.command;

public class CannotDockAShipThatIsNotSailingException extends IllegalStateException {

    public CannotDockAShipThatIsNotSailingException() {

        super("A ship cannot be docked if it is not sailing");
    }
}
