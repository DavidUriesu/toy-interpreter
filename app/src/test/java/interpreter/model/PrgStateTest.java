package interpreter.model;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {
    @Test
    void constructorPlaceProgramOnStack() throws ADTException {
        IStmt program = new NopStmt();
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);

        assertFalse(state.getExeStack().isEmpty());
        assertSame(program, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
        assertSame(program, state.getOriginalProgram());
    }

    @Test
    void constructorUsesProvidedStructures() {
        MyStack<IStmt> stack = new MyStack<>();
        MyDictionary<String, IValue> symbols = new MyDictionary<>();
        MyList<IValue> output = new MyList<>();
        PrgState state = new PrgState(stack, symbols, output, new NopStmt());

        assertSame(stack, state.getExeStack());
        assertSame(symbols, state.getSymTable());
        assertSame(output, state.getOut());
        assertEquals(0, output.size());
        assertFalse(symbols.isDefined("x"));
    }
}
