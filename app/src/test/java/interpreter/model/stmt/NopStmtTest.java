package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    @Test
    void executionLeavesStateUnchanged() throws MyException {
        IStmt statement = new NopStmt();
        PrgState state = PrgState.of(statement);

        state.getSymTable().put("x", new IntValue(10));
        state.getOut().add(new IntValue(20));
        state.getExeStack().pop(); // Simulate the controller removing the next instruction.

        PrgState result = statement.execute(state);

        assertSame(state, result);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(20), state.getOut().get(0));
    }
}
