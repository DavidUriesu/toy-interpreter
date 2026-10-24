package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    @Test
    void newListHasSizeZero() {
        IList<Integer> list = new MyList<>();
        assertEquals(0, list.size());
    }

    @Test
    void addPreservesInsertionOrder() throws ADTException {
        IList<Integer> list = new MyList<>();

        list.add(10);
        list.add(20);

        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void getFromEmptyListThrows() {
        IList<Integer> list = new MyList<>();
        assertThrows(ADTException.class, () -> list.get(0));
    }

    @Test
    void negativeIndexThrows() {
        IList<Integer> list = new MyList<>();
        list.add(10);
        assertThrows(ADTException.class, () -> list.get(-1));
    }

    @Test
    void indexEqualToSizeThrows() {
        IList<Integer> list = new MyList<>();
        list.add(10);
        assertThrows(ADTException.class, () -> list.get(1));
    }
}
