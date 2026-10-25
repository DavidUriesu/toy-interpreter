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

class LogicExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private static IExp bool(boolean value) {
        return new ValueExp(new BoolValue(value));
    }

    @Test
    void andRequiresBothValuesToBeTrue() throws MyException {
        assertEquals(new BoolValue(true), new LogicExp(LogicOp.AND, bool(true), bool(true)).eval(symTable));
        assertEquals(new BoolValue(false), new LogicExp(LogicOp.AND, bool(true), bool(false)).eval(symTable));
        assertEquals(new BoolValue(false), new LogicExp(LogicOp.AND, bool(false), bool(true)).eval(symTable));
        assertEquals(new BoolValue(false), new LogicExp(LogicOp.AND, bool(false), bool(false)).eval(symTable));
    }

    @Test
    void orRequiresOneValueToBeTrue() throws MyException {
        assertEquals(new BoolValue(true), new LogicExp(LogicOp.OR, bool(true), bool(true)).eval(symTable));
        assertEquals(new BoolValue(true), new LogicExp(LogicOp.OR, bool(true), bool(false)).eval(symTable));
        assertEquals(new BoolValue(true), new LogicExp(LogicOp.OR, bool(false), bool(true)).eval(symTable));
        assertEquals(new BoolValue(false), new LogicExp(LogicOp.OR, bool(false), bool(false)).eval(symTable));
    }

    @Test
    void evaluatesVariableOperand() throws MyException {
        symTable.put("ready", new BoolValue(true));
        IExp expression = new LogicExp(LogicOp.AND, new VarExp("ready"), bool(true));
        assertEquals(new BoolValue(true), expression.eval(symTable));
    }

    @Test
    void nonBooleanLeftOperandThrows() {
        IExp expression = new LogicExp(LogicOp.AND, new ValueExp(new IntValue(1)), bool(true));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("First operand is not a boolean", error.getMessage());
    }

    @Test
    void nonBooleanRightOperandThrows() {
        IExp expression = new LogicExp(LogicOp.AND, bool(false), new ValueExp(new IntValue(1)));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("Second operand is not a boolean", error.getMessage());
    }

    @Test
    void andStillEvaluatesRightOperandWhenLeftIsFalse() {
        IExp expression = new LogicExp(LogicOp.AND, bool(false), new VarExp("missing"));
        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }

    @Test
    void orStillEvaluatesRightOperandWhenLeftIsTrue() {
        IExp expression = new LogicExp(LogicOp.OR, bool(true), new VarExp("missing"));
        assertThrows(ExpressionException.class, () -> expression.eval(symTable));
    }
}

