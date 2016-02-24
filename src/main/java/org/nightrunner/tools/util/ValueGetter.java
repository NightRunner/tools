package org.nightrunner.tools.util;

public interface ValueGetter<T, R> {
    R getValue(T object);
}
