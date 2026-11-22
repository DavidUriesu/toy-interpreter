package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapOperationsTest {
    private final PrgState state = PrgState.of(new NopStmt());
    private final ValueExp twenty = new ValueExp(new IntValue(20));
    private final ReadHeapExp readV = new ReadHeapExp(new VarExp("v"));

    @BeforeEach
    void declareVariables() throws MyException {
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @Test
    void newAllocatesAndUpdatesTheReference() throws MyException {
        new NewStmt("v", twenty).execute(state);

        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newRejectsAnUndeclaredVariable() {
        assertThrows(StatementException.class,
                () -> new NewStmt("missing", twenty).execute(state));
    }

    @Test
    void newRejectsANonReferenceVariable() {
        assertThrows(StatementException.class,
                () -> new NewStmt("x", twenty).execute(state));
    }

    @Test
    void newRejectsTheWrongValueTypeWithoutAllocating() throws MyException {
        var bool = new ValueExp(new BoolValue(true));

        assertThrows(StatementException.class,
                () -> new NewStmt("v", bool).execute(state));

        assertTrue(state.getHeap().getContent().isEmpty());
        assertEquals(new RefValue(0, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void readReturnsTheStoredValue() throws MyException {
        new NewStmt("v", twenty).execute(state);

        assertEquals(new IntValue(20), readV.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void readRejectsANonReferenceExpression() {
        var read = new ReadHeapExp(twenty);

        assertThrows(ExpressionException.class,
                () -> read.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void readRejectsAnUninitializedReference() {
        assertThrows(ExpressionException.class,
                () -> readV.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void writeChangesTheValueAtTheSameAddress() throws MyException {
        new NewStmt("v", twenty).execute(state);
        var reference = state.getSymTable().lookup("v");

        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);

        assertEquals(reference, state.getSymTable().lookup("v"));
        assertEquals(1, state.getHeap().getContent().size());
        assertEquals(new IntValue(30), readV.eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void writeRejectsAnUndeclaredVariable() {
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("missing", twenty).execute(state));
    }

    @Test
    void writeRejectsANonReferenceVariable() {
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("x", twenty).execute(state));
    }

    @Test
    void writeRejectsAnUninitializedReference() {
        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", twenty).execute(state));
    }

    @Test
    void writeRejectsTheWrongTypeAndPreservesTheStoredValue() throws MyException {
        new NewStmt("v", twenty).execute(state);
        var bool = new ValueExp(new BoolValue(true));

        assertThrows(StatementException.class,
                () -> new WriteHeapStmt("v", bool).execute(state));

        assertEquals(new IntValue(20), state.getHeap().get(1));
    }
}