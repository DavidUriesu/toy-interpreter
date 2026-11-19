package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyHeapTest {
    private final IHeap heap = new MyHeap();

    @Test
    void allocateReturnsConsecutiveAddressesStartingAtOne() {
        assertEquals(1, heap.allocate(new IntValue(20)));
        assertEquals(2, heap.allocate(new IntValue(30)));
        assertEquals(3, heap.allocate(new IntValue(40)));
    }

    @Test
    void getReturnsTheValueStoredAtTheAddress() throws ADTException {
        var value = new IntValue(20);
        int address = heap.allocate(value);

        assertEquals(value, heap.get(address));
    }

    @Test
    void updateReplacesTheValueAtTheSameAddress() throws ADTException {
        int address = heap.allocate(new IntValue(20));
        var replacement = new IntValue(30);

        heap.update(address, replacement);

        assertEquals(replacement, heap.get(address));
    }

    @Test
    void getAndUpdateThrowForMissingAddresses() {
        assertThrows(ADTException.class, () -> heap.get(0));
        assertThrows(ADTException.class, () -> heap.update(0, new IntValue(20)));

        assertThrows(ADTException.class, () -> heap.get(1));
        assertThrows(ADTException.class, () -> heap.update(1, new IntValue(20)));
    }
}