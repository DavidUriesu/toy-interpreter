package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
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

class IfStmtTest {

    private final IStmt remaining = new NopStmt();

    private final PrgState state = new PrgState(
            new MyStack<>(),
            new MyDictionary<>(),
            new MyList<>(),
            remaining
    );

    private final IStmt thenBranch = new PrintStmt(new ValueExp(new IntValue(1)));

    private final IStmt elseBranch = new PrintStmt(new ValueExp(new IntValue(2)));

    @Test
    void trueConditionSchedulesOnlyThenBranch() throws MyException {
        IStmt statement = new IfStmt(
                new ValueExp(new BoolValue(true)),
                thenBranch,
                elseBranch
        );

        assertSame(state, statement.execute(state));

        assertSame(thenBranch, state.getExeStack().pop());
        assertSame(remaining, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
    }

    @Test
    void falseConditionSchedulesOnlyElseBranch() throws MyException {
        IStmt statement = new IfStmt(
                new ValueExp(new BoolValue(false)),
                thenBranch,
                elseBranch
        );

        statement.execute(state);

        assertSame(elseBranch, state.getExeStack().pop());
        assertSame(remaining, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
    }

    @Test
    void nonBooleanConditionThrowsWithoutSchedulingBranch() throws MyException {
        IStmt statement = new IfStmt(
                new ValueExp(new IntValue(1)),
                thenBranch,
                elseBranch
        );

        assertThrows(StatementException.class, () -> statement.execute(state));
        assertSame(remaining, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void undefinedConditionVariableThrowsWithoutSchedulingBranch() throws MyException {
        IStmt statement = new IfStmt(
                new VarExp("missing"),
                thenBranch,
                elseBranch
        );

        assertThrows(ExpressionException.class, () -> statement.execute(state));

        assertSame(remaining, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }
}