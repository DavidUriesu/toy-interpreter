package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

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
}
