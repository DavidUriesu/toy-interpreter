package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    @Test
    void newDictionaryHasNoDefinedKeys() {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        assertFalse(dictionary.isDefined("x"));
    }

    @Test
    void putStoresValue() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);

        assertTrue(dictionary.isDefined("x"));
        assertEquals(10, dictionary.lookup("x"));
    }

    @Test
    void updateReplacesExistingValue() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);

        dictionary.update("x", 20);
        assertEquals(20, dictionary.lookup("x"));
    }

    @Test
    void updateWithSameValueIsAllowed() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);

        dictionary.update("x", 10);
        assertEquals(10, dictionary.lookup("x"));
    }

    @Test
    void lookupMissingKeyThrows() {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        assertThrows(ADTException.class, () -> dictionary.lookup("x"));
    }

    @Test
    void putDuplicateKeyThrowsAndPreservesValue() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);

        assertThrows(ADTException.class, () -> dictionary.put("x", 20));
        assertEquals(10, dictionary.lookup("x"));
    }

    @Test
    void updateMissingKeyThrowsAndDoesNotInsertIt() {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        assertThrows(ADTException.class, () -> dictionary.update("x", 20));
        assertFalse(dictionary.isDefined("x"));
    }

    @Test
    void removeDeletesExistingKey() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);
        dictionary.put("y", 20);

        dictionary.remove("x");

        assertFalse(dictionary.isDefined("x"));
        assertEquals(20, dictionary.lookup("y"));
    }

    @Test
    void removeMissingKeyThrows() {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        assertThrows(ADTException.class, () -> dictionary.remove("x"));
    }

    @Test
    void toMapReturnsUnmodifiableSnapshot() throws ADTException {
        IDictionary<String, Integer> dictionary = new MyDictionary<>();
        dictionary.put("x", 10);

        Map<String, Integer> snapshot = dictionary.toMap();
        dictionary.update("x", 20);

        assertEquals(10, snapshot.get("x"));
        assertThrows(UnsupportedOperationException.class, () -> snapshot.put("y", 30));
        assertFalse(dictionary.isDefined("y"));
    }
}
