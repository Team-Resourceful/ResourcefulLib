package com.teamresourceful.resourcefullib.common.exceptions;

public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        super("This method is not implemented for this platform.");
    }
}
