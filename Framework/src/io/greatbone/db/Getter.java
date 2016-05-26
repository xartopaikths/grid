package io.greatbone.db;

/**
 */
public interface Getter<T> {

    T get(ResultSetWrap wrap);

}
