package dev.enricosola.yummy.exception;

public class MisconfigurationException extends RuntimeException {
    public MisconfigurationException(String message){
        super(message);
    }
}
