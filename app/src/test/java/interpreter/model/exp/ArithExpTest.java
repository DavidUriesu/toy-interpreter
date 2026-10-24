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

class ArithExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private static IExp num(int value) {
        return new ValueExp(new IntValue(value));
    }

    @Test
    void addsIntegers() throws MyException{
        IExp expression = new ArithExp(ArithOp.ADD, num(7), num(2));
        assertEquals(new IntValue(9), expression.eval(symTable));
    }

    @Test
    void subtractsIntegers() throws MyException{
        IExp expression = new ArithExp(ArithOp.SUB, num(7), num(2));
        assertEquals(new IntValue(5), expression.eval(symTable));
    }

    @Test
    void multipliesIntegers() throws MyException{
        IExp expression = new ArithExp(ArithOp.MUL, num(7), num(2));
        assertEquals(new IntValue(14), expression.eval(symTable));
    }

    @Test
    void dividesIntegers() throws MyException{
        IExp expression = new ArithExp(ArithOp.DIV, num(7), num(2));
        assertEquals(new IntValue(3), expression.eval(symTable));
    }

    @Test
    void evaluatesNestedExpression() throws MyException {
        IExp expression = new ArithExp(ArithOp.ADD, num(2), new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), expression.eval(symTable));
    }

    @Test
    void evaluateVariableOperand() throws MyException {
        symTable.put("x", new IntValue(10));
        IExp expression = new ArithExp(ArithOp.ADD, new VarExp("x"), num(2));
        assertEquals(new IntValue(12), expression.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp expression = new ArithExp(ArithOp.DIV, num(7), num(0));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("Division by zero", error.getMessage());
    }

    @Test
    void nonIntegerLeftOperandThrows() {
        IExp expression = new ArithExp(ArithOp.ADD, new ValueExp(new BoolValue(true)), num(2));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("First operand is not an integer", error.getMessage());
    }

    @Test
    void nonIntegerRightOperandThrows() {
        IExp expression = new ArithExp(ArithOp.ADD, num(2), new ValueExp(new BoolValue(true)));
        ExpressionException error = assertThrows(ExpressionException.class, () -> expression.eval(symTable));
        assertEquals("Second operand is not an integer", error.getMessage());
    }
}
