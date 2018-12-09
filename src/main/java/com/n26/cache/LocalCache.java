package com.n26.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalCache<K,V> implements CacheManager<K,V> {
    Map<K,V> cacheMap = null;

    public LocalCache() {
        cacheMap = new HashMap<K,V>();
    }

    @Override
    public V add(K key, V value) {
        return cacheMap.put(key, value);
    }

    @Override
    public V remove(K key) {
        return cacheMap.remove(key);
    }

    @Override
    public boolean removeAll() {
        cacheMap = new HashMap<>();
        return Boolean.TRUE;
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    public Set<K> keySet() {
        return cacheMap.keySet();
    }
}