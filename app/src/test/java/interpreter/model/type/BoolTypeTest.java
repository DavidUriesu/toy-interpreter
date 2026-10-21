package interpreter.model.type;

import interpreter.model.value.BoolValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoolTypeTest {
    @Test
    void defaultValueIsFalse() {
        BoolType type = new BoolType();
        assertEquals(new BoolValue(false), type.defaultValue());
    }

    @Test
    void booleanTypesAreEqual() {
        assertEquals(new BoolType(), new BoolType());
    }
}
