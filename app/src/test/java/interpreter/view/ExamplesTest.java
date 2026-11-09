package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamplesTest {
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        controller.allStep();

        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.example1());

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.example2());

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.example3());

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void fileExamplePrintsBothNumbersAndClosesFile() throws Exception {
        Path input = dir.resolve("numbers.in");
        Files.writeString(input, "15\n50\n");

        PrgState state = run(Examples.fileExample(input.toString()));

        assertEquals(
                List.of(new IntValue(15), new IntValue(50)),
                state.getOut().toList());
        assertTrue(state.getExeStack().isEmpty());
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExamplePrintsZerosForEmptyFile() throws Exception {
        Path input = dir.resolve("empty.in");
        Files.writeString(input, "");

        PrgState state = run(Examples.fileExample(input.toString()));

        assertEquals(
                List.of(new IntValue(0), new IntValue(0)),
                state.getOut().toList());
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example5PrintsFive() throws MyException {
        PrgState state = run(Examples.example5());

        assertEquals(List.of(new IntValue(5)), state.getOut().toList());
        assertTrue(state.getExeStack().isEmpty());
    }
}