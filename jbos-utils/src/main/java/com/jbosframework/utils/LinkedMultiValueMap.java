package com.jbosframework.utils;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable, Cloneable {
    private static final long serialVersionUID = 3801124242820219131L;
    private final Map<K, List<V>> targetMap;

    public LinkedMultiValueMap() {
        this.targetMap = new LinkedHashMap();
    }

    public LinkedMultiValueMap(int initialCapacity) {
        this.targetMap = new LinkedHashMap(initialCapacity);
    }

    public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
        this.targetMap = new LinkedHashMap(otherMap);
    }

    @Nullable
    public V getFirst(K key) {
        List<V> values = (List)this.targetMap.get(key);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public void add(K key, @Nullable V value) {
        List<V> values = (List)this.targetMap.computeIfAbsent(key, (k) -> {
            return new LinkedList();
        });
        values.add(value);
    }

    public void addAll(K key, List<? extends V> values) {
        List<V> currentValues = (List)this.targetMap.computeIfAbsent(key, (k) -> {
            return new LinkedList();
        });
        currentValues.addAll(values);
    }

    public void addAll(MultiValueMap<K, V> values) {
        Iterator var2 = values.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<K, List<V>> entry = (Map.Entry)var2.next();
            this.addAll(entry.getKey(), (List)entry.getValue());
        }

    }

    public void set(K key, @Nullable V value) {
        List<V> values = new LinkedList();
        values.add(value);
        this.targetMap.put(key, values);
    }

    public void setAll(Map<K, V> values) {
        values.forEach(this::set);
    }

    public Map<K, V> toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap(this.targetMap.size());
        this.targetMap.forEach((key, values) -> {
            if (values != null && !values.isEmpty()) {
                singleValueMap.put(key, values.get(0));
            }

        });
        return singleValueMap;
    }

    public int size() {
        return this.targetMap.size();
    }

    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    @Nullable
    public List<V> get(Object key) {
        return (List)this.targetMap.get(key);
    }

    @Nullable
    public List<V> put(K key, List<V> value) {
        return (List)this.targetMap.put(key, value);
    }

    @Nullable
    public List<V> remove(Object key) {
        return (List)this.targetMap.remove(key);
    }

    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.targetMap.putAll(map);
    }

    public void clear() {
        this.targetMap.clear();
    }

    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    public Set<Map.Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    public LinkedMultiValueMap<K, V> deepCopy() {
        LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap(this.targetMap.size());
        this.targetMap.forEach((key, value) -> {
            copy.put(key, (List)(new LinkedList(value)));
        });
        return copy;
    }

    public LinkedMultiValueMap<K, V> clone() {
        return new LinkedMultiValueMap(this);
    }

    public boolean equals(Object obj) {
        return this.targetMap.equals(obj);
    }

    public int hashCode() {
        return this.targetMap.hashCode();
    }

    public String toString() {
        return this.targetMap.toString();
    }
}
