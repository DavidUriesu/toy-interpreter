package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringValueTest {
    @Test
    void returnsStringType() {
        assertEquals(new StringType(), new StringValue("hello").getType());
    }

    @Test
    void convertsToQuotedString() {
        assertEquals("\"hello\"", new StringValue("hello").toString());
    }

    @Test
    void equalValuesHaveEqualHashCodes() {
        StringValue first = new StringValue("test.in");
        StringValue second = new StringValue("test.in");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
