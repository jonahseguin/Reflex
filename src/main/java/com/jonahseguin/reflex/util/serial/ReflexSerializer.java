/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.util.serial;

public abstract class ReflexSerializer<T> {

    public abstract String toString(T object);

    public abstract T fromString(Object object, Class<? extends T> type);

}
