package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private final PrgState state = new PrgState(
            new MyStack<>(),
            new MyDictionary<>(),
            new MyList<>(),
            new NopStmt()
    );

    @Test
    void integerDeclarationStoresZero() throws MyException {
        IStmt statement = new VarDeclStmt("x", new IntType());
        assertSame(state, statement.execute(state));
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void booleanDeclarationStoresFalse() throws MyException {
        IStmt statement = new VarDeclStmt("ready", new BoolType());
        statement.execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("ready"));
    }

    @Test
    void redeclarationThrowsAndPreserves() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        state.getSymTable().update("x", new IntValue(10));

        IStmt duplicate = new VarDeclStmt("x", new BoolType());

        assertThrows(StatementException.class, () -> duplicate.execute(state));
        assertEquals(new IntValue(10), state.getSymTable().lookup("x"));
    }
}
