package interpreter.model.value;

import interpreter.model.type.BoolType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoolValueTest {
    @Test
    void returnsBooleanType() {
        BoolValue value = new BoolValue(true);
        assertEquals(new BoolType(), value.getType());
    }

    @Test
    void convertsToString() {
        BoolValue value = new BoolValue(false);
        assertEquals("false", value.toString());
    }
}
