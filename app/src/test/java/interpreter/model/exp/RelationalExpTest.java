package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RelationalExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp num(int value) {
        return new ValueExp(new IntValue(value));
    }

    @ParameterizedTest
    @CsvSource({
            "LT, 5, 7, true",
            "LT, 7, 7, false",
            "LE, 7, 7, true",
            "LE, 8, 7, false",
            "EQ, 7, 7, true",
            "EQ, 5, 7, false",
            "NE, 5, 7, true",
            "NE, 7, 7, false",
            "GT, 8, 7, true",
            "GT, 7, 7, false",
            "GE, 7, 7, true",
            "GE, 5, 7, false"
    })
    void comparesIntegers(RelOp op, int left, int right, boolean expected) throws MyException {
        IExp expression = new RelationalExp(op, num(left), num(right));
        assertEquals(new BoolValue(expected), expression.eval(symTable));
    }

    @Test
    void nonIntegerLeftOperandThrows() {
        IExp expression = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), num(7));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("First operand is not an integer", error.getMessage());
    }

    @Test
    void nonIntegerRightOperandThrows() {
        IExp expression = new RelationalExp(RelOp.LT, num(7), new ValueExp(new BoolValue(true)));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("Second operand is not an integer", error.getMessage());
    }

    @Test
    void convertsToInfixString() {
        IExp expression = new RelationalExp(RelOp.LT, num(5), num(7));
        assertEquals("(5 < 7)", expression.toString());
    }
}
