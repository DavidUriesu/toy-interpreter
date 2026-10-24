package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> dictionary = new HashMap<>();

    @Override
    public boolean isDefined(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!isDefined(key)) {
            throw new ADTException("Key is not defined: " + key);
        }

        return dictionary.get(key);
    }

    @Override
    public void put(K key, V value) throws ADTException {
        if (isDefined(key)) {
            throw new ADTException("Key is already defined: " + key);
        }

        dictionary.put(key, value);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!isDefined(key)) {
            throw new ADTException("Key is not defined: " + key);
        }

        dictionary.put(key, value);
    }

    @Override
    public String toString() {
        return dictionary.toString();
    }
}
