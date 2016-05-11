package com.shawckz.reflex.core.configuration.exceptions;


public class NotASerializerException extends Exception {

    public NotASerializerException() {
        super( "Config given an object which doesn't extend serializer" );
    }
}
