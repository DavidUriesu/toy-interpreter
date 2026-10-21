package interpreter.model.value;

import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntValueTest {
    @Test
    void returnsIntegerType() {
        IntValue value = new IntValue(5);
        assertEquals(new IntType(), value.getType());
    }

    @Test
    void convertsToString() {
        IntValue value = new IntValue(5);
        assertEquals("5", value.toString());
    }
}
