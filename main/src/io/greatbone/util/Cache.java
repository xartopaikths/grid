package io.greatbone.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 */
public abstract class Cache<K, V extends Cacheable> {

    AtomicReferenceArray<V> objects;

    public void loadAll() {

    }

    public V get(K key) {
        return null;
    }

    public V getThrough(K key) {
        return null;
    }

    public V[] search(K[] key) {
        return null;
    }

    public V[] search() {
        return null;
    }

}
