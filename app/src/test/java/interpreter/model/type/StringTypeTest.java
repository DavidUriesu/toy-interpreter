package interpreter.model.type;

import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringTypeTest {
    @Test
    void defaultValueIsEmptyString() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }
}
