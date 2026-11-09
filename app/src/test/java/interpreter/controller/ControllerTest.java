package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import interpreter.exceptions.RepositoryException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @TempDir
    Path dir;

    private static PrgState createState(IStmt program) {
        return PrgState.of(program);
    }

    @Test
    void repositoryReturnsProvidedState() {
        PrgState state = createState(new NopStmt());
        Repository repository = new Repository(state, dir.resolve("log.txt").toString());

        assertSame(state, repository.getCrtPrg());
    }

    @Test
    void oneStepPopsAndExecutesStatement() throws MyException {
        PrgState state = createState(
                new VarDeclStmt("x", new IntType())
        );

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        assertSame(state, controller.oneStep(state));

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void oneStepOnEmptyStackThrows() throws MyException {
        PrgState state = createState(new NopStmt());
        state.getExeStack().pop();

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        assertThrows(
                StatementException.class,
                () -> controller.oneStep(state)
        );
    }

    @Test
    void oneStepExpandsCompositionWithoutExecutingItsChildren()
            throws MyException {

        IStmt first = new VarDeclStmt("x", new IntType());
        IStmt second = new PrintStmt(new VarExp("x"));

        PrgState state = createState(new CompStmt(first, second));

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        controller.oneStep(state);

        assertFalse(state.getSymTable().isDefined("x"));
        assertEquals(0, state.getOut().size());
        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void allStepExecutesDeclarationAssignmentAndPrint()
            throws MyException {

        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt(
                                "v", new ValueExp(new IntValue(2))
                        ),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void executionErrorStopsBeforeFollowingStatement() {
        IStmt program = new CompStmt(
                new AssignStmt(
                        "missing", new ValueExp(new IntValue(10))
                ),
                new PrintStmt(new ValueExp(new IntValue(99)))
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state, dir.resolve("log.txt").toString()), false
        );

        assertThrows(
                StatementException.class,
                () -> controller.allStep()
        );

        assertEquals(0, state.getOut().size());
        assertFalse(state.getSymTable().isDefined("missing"));
        assertFalse(state.getExeStack().isEmpty());
    }

    @Test
    void logsInitialStateAndEveryStepWithDisplayDisabled() throws Exception {
        IStmt program = new CompStmt(
                new VarDeclStmt("x", new IntType()),
                new PrintStmt(new VarExp("x"))
        );

        PrgState state = createState(program);
        Path log = dir.resolve("log.txt");

        Controller controller = new Controller(
                new Repository(state, log.toString()), false);

        controller.allStep();

        String contents = Files.readString(log);

        // Initial state + composition + declaration + print = four states.
        assertEquals(4, contents.lines()
                .filter(line -> line.equals("ExeStack:"))
                .count());

        int stack = contents.indexOf("ExeStack:");
        int symbols = contents.indexOf("SymTable:");
        int output = contents.indexOf("Out:");
        int files = contents.indexOf("FileTable:");

        assertTrue(stack >= 0 && stack < symbols
                && symbols < output && output < files);

        assertTrue(contents.endsWith(
                state.toLogString() + System.lineSeparator()));
    }

    @Test
    void loggingAppendsInsteadOfOverwriting() throws Exception {
        PrgState state = createState(new NopStmt());
        Path log = dir.resolve("log.txt");
        Repository repository = new Repository(state, log.toString());

        repository.logPrgStateExec();
        String firstWrite = Files.readString(log);

        repository.logPrgStateExec();

        assertEquals(firstWrite + firstWrite, Files.readString(log));
    }

    @Test
    void invalidLogPathThrowsRepositoryException() {
        PrgState state = createState(new NopStmt());

        // A directory cannot be opened as a regular log file.
        Repository repository = new Repository(state, dir.toString());

        assertThrows(RepositoryException.class,
                () -> repository.logPrgStateExec());
    }
}