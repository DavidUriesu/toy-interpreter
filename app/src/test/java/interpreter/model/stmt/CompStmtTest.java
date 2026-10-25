package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {

    @Test
    void schedulesFirstThenSecondBeforeRemainingInstructions() throws MyException {
        IStmt first = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt second = new PrintStmt(new ValueExp(new IntValue(2)));
        IStmt remaining = new NopStmt();

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                remaining
        );

        IStmt compound = new CompStmt(first, second);

        assertSame(state, compound.execute(state));
        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertSame(remaining, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
    }
}