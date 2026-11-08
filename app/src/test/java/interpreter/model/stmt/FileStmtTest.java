package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;

    private PrgState state;
    private Path input;
    private IExp file;

    @BeforeEach
    void setUp() throws Exception {
        input = dir.resolve("numbers.in");
        Files.writeString(input, "15\n50\n");
        file = new ValueExp(new StringValue(input.toString()));

        state = PrgState.of(new NopStmt());
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @AfterEach
    void closeRemainingReaders() throws IOException {
        for (var reader : state.getFileTable().toMap().values()) {
            reader.close();
        }
    }

    @Test
    void opensFileAndFindsItByEqualFilename() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().isDefined(
                new StringValue(input.toString())));
    }

    @Test
    void openingSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void openingMissingFileThrows() {
        IExp missing = new ValueExp(
                new StringValue(dir.resolve("missing.in").toString()));

        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileStatementsRejectNonStringFilename() {
        IExp number = new ValueExp(new IntValue(1));

        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(number).execute(state));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(number, "x").execute(state));
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(number).execute(state));
    }

    @Test
    void readsNumbersInOrderThenZeroAtEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("x"));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void emptyFileGivesZero() throws Exception {
        Files.writeString(input, "");
        new OpenRFileStmt(file).execute(state);

        new ReadFileStmt(file, "x").execute(state);

        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readRejectsUndeclaredVariableWithoutConsumingLine()
            throws MyException {
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "missing").execute(state));

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));
    }

    @Test
    void readRejectsNonIntegerVariable() throws MyException {
        new VarDeclStmt("text", new StringType()).execute(state);
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "text").execute(state));
    }

    @Test
    void readingUnopenedFileThrows() {
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void invalidNumberThrowsAndPreservesVariable() throws Exception {
        Files.writeString(input, "abc\n");
        state.getSymTable().update("x", new IntValue(99));
        new OpenRFileStmt(file).execute(state);

        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));

        assertEquals(new IntValue(99), state.getSymTable().lookup("x"));
    }

    @Test
    void closingFileClosesReaderAndRemovesEntry() throws Exception {
        new OpenRFileStmt(file).execute(state);
        var reader = state.getFileTable().lookup(
                new StringValue(input.toString()));

        new CloseRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().toMap().isEmpty());
        assertThrows(IOException.class, () -> reader.readLine());
    }

    @Test
    void closingUnopenedFileThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void readIoErrorBecomesStatementException() throws Exception {
        new OpenRFileStmt(file).execute(state);
        state.getFileTable().lookup(
                new StringValue(input.toString())).close();

        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));
    }
}