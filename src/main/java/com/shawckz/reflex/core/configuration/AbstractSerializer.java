package com.shawckz.reflex.core.configuration;


public abstract class AbstractSerializer<T> {

    public AbstractSerializer() {
    }

    public abstract String toString(T data);

    public abstract T fromString(Object data);

}
