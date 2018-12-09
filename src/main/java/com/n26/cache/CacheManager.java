package com.n26.cache;

import java.util.Set;

public interface CacheManager<K,V> {
    V add(K key, V value);
    V remove(K key);
    boolean removeAll();
    V get(K key);
    Set<K> keySet();
}
