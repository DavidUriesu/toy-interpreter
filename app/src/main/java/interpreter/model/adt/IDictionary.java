package interpreter.model.adt;

import java.util.Map;
import interpreter.exceptions.ADTException;

public interface IDictionary<K, V> {
    boolean isDefined(K key);

    V lookup(K key) throws ADTException;

    void put(K key, V value) throws ADTException;

    void update(K key, V value) throws ADTException;

    void remove(K key) throws ADTException;

    Map<K, V> toMap();
}
