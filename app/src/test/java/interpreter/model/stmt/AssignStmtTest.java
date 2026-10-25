package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest {

    private final PrgState state = new PrgState(
            new MyStack<>(),
            new MyDictionary<>(),
            new MyList<>(),
            new NopStmt()
    );

    @Test
    void assignsIntegerValue() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);

        IStmt statement = new AssignStmt("x", new ValueExp(new IntValue(10)));

        assertSame(state, statement.execute(state));
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
    }

    @Test
    void assignsBooleanValue() throws MyException {
        new VarDeclStmt("ready", new BoolType()).execute(state);
        new AssignStmt("ready", new ValueExp(new BoolValue(true))).execute(state);
        assertEquals(new BoolValue(true), state.getSymTable().lookup("ready"));
    }

    @Test
    void evaluatesExpressionBeforeAssignment() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        state.getSymTable().update("x", new IntValue(10));

        IStmt statement = new AssignStmt("x", new ArithExp(ArithOp.ADD, new VarExp("x"), new ValueExp(new IntValue(2))));
        statement.execute(state);

        assertEquals(new IntValue(12), state.getSymTable().lookup("x"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        IStmt statement = new AssignStmt(
                "missing", new ValueExp(new IntValue(10))
        );

        assertThrows(
                StatementException.class,
                () -> statement.execute(state)
        );

        assertFalse(state.getSymTable().isDefined("missing"));
    }

    @Test
    void typeMismatchThrowsAndPreservesValue() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        state.getSymTable().update("x", new IntValue(10));

        IStmt statement = new AssignStmt(
                "x", new ValueExp(new BoolValue(true))
        );

        assertThrows(
                StatementException.class,
                () -> statement.execute(state)
        );

        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
    }

    @Test
    void expressionErrorPreservesPreviousValue() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        state.getSymTable().update("x", new IntValue(10));

        IStmt statement = new AssignStmt(
                "x",
                new ArithExp(
                        ArithOp.DIV,
                        new ValueExp(new IntValue(5)),
                        new ValueExp(new IntValue(0))
                )
        );

        assertThrows(
                ExpressionException.class,
                () -> statement.execute(state)
        );

        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
    }
}