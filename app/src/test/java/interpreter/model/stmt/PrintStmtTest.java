package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {

    private final PrgState state = new PrgState(
            new MyStack<>(),
            new MyDictionary<>(),
            new MyList<>(),
            new NopStmt()
    );

    @Test
    void appendsValuesInOrder() throws MyException {
        IStmt first = new PrintStmt(new ValueExp(new IntValue(10)));
        IStmt second = new PrintStmt(new ValueExp(new BoolValue(true)));

        assertSame(state, first.execute(state));
        second.execute(state);

        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(10), state.getOut().get(0));
        assertEquals(new BoolValue(true), state.getOut().get(1));
    }

    @Test
    void printedValueIsPreservedAfterVariableChanges() throws MyException {
        state.getSymTable().put("x", new IntValue(10));

        new PrintStmt(new VarExp("x")).execute(state);

        state.getSymTable().update("x", new IntValue(20));

        assertEquals(new IntValue(10), state.getOut().get(0));
        assertEquals(new IntValue(20), state.getSymTable().lookup("x"));
    }

    @Test
    void expressionErrorLeavesOutputUnchanged() throws MyException {
        state.getOut().add(new IntValue(5));

        IStmt statement = new PrintStmt(new VarExp("missing"));

        assertThrows(ExpressionException.class, () -> statement.execute(state));

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }
}