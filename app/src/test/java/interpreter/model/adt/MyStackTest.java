package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {
    @Test
    void newStackIsEmpty() {
        IStack<Integer> stack = new MyStack<>();
        assertTrue(stack.isEmpty());
    }

    @Test
    void popsInReverseInsertionOrder() throws ADTException {
        IStack<Integer> stack = new MyStack<>();

        stack.push(10);
        stack.push(20);

        assertFalse(stack.isEmpty());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void popOnEmptyStackThrows() {
        IStack<Integer> stack = new MyStack<>();
        assertThrows(ADTException.class, () -> stack.pop());
    }

    @Test
    void toListReturnsTopFirstWithoutChangingStack() throws ADTException {
        IStack<Integer> stack = new MyStack<>();
        stack.push(10);
        stack.push(20);

        List<Integer> snapshot = stack.toList();

        assertEquals(List.of(20, 10), snapshot);
        assertEquals(20, stack.pop());
        assertEquals(List.of(20, 10), snapshot);
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(30));
    }
}
