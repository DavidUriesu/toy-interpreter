package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueExpTest {
    @Test
    void evaluatesIntegerConstant() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IExp expression = new ValueExp(new IntValue(42));
        assertEquals(new IntValue(42), expression.eval(symTable));
    }

    @Test
    void evaluatesBooleanConstant() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IExp expression = new ValueExp(new BoolValue(true));
        assertEquals(new BoolValue(true), expression.eval(symTable));
    }
}
