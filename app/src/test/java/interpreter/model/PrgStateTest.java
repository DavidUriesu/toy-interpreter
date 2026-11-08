package interpreter.model;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.PipedReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {
    @Test
    void factoryPlacesProgramOnStack() throws ADTException {
        IStmt program = new NopStmt();
        PrgState state = PrgState.of(program);

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
        var files = new MyDictionary<StringValue, BufferedReader>();
        PrgState state = new PrgState(stack, symbols, output, files, new NopStmt());

        assertSame(stack, state.getExeStack());
        assertSame(symbols, state.getSymTable());
        assertSame(output, state.getOut());
        assertSame(files, state.getFileTable());
        assertEquals(0, output.size());
        assertFalse(symbols.isDefined("x"));
    }

    @Test
    void factoryStartsWithEmptyDataStructures() {
        PrgState state = PrgState.of(new NopStmt());

        assertTrue(state.getSymTable().toMap().isEmpty());
        assertEquals(0, state.getOut().size());
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void factoryCreatesIndependentStructures() {
        IStmt program = new NopStmt();

        PrgState first = PrgState.of(program);
        PrgState second = PrgState.of(program);

        assertNotSame(first.getExeStack(), second.getExeStack());
        assertNotSame(first.getSymTable(), second.getSymTable());
        assertNotSame(first.getOut(), second.getOut());
        assertNotSame(first.getFileTable(), second.getFileTable());
    }
}
