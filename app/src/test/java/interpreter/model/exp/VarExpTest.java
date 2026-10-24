package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VarExpTest {
    @Test
    void evaluatesIntegerVariable() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        symTable.put("x", new IntValue(10));
        IExp expression = new VarExp("x");

        assertEquals(new IntValue(10), expression.eval(symTable));
    }

    @Test
    void evaluatesBooleanVariable() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        symTable.put("ready", new BoolValue(true));
        IExp expression = new VarExp("ready");

        assertEquals(new BoolValue(true), expression.eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        IExp expression = new VarExp("missing");
        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }

    @Test
    void evaluatesCurrentValueAfterUpdate() throws MyException {
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        symTable.put("x", new IntValue(10));
        IExp expression = new VarExp("x");

        symTable.update("x", new IntValue(20));

        assertEquals(new IntValue(20), expression.eval(symTable));
    }
}
