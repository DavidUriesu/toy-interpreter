package interpreter.model;

import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {
    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private final IStmt originalProgram;

    public PrgState(IStack<IStmt> exeStack,
                     IDictionary<String, IValue> symTable,
                     IList<IValue> out,
                     IDictionary<StringValue, BufferedReader> fileTable,
                     IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = program;

        exeStack.push(program);
    }

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public static PrgState of(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                program
        );
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack
                + "\nSymTable: " + symTable
                + "\nOut: " + out
                + "\nFileTable: " + fileTable;
    }

    public String toLogString() {
        var sb = new StringBuilder();

        sb.append("ExeStack:\n");
        for (var statement : exeStack.toList()) {
            sb.append(statement).append("\n");
        }

        sb.append("SymTable:\n");
        for (var entry : symTable.toMap().entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }

        sb.append("Out:\n");
        for (var value : out.toList()) {
            sb.append(value).append("\n");
        }

        sb.append("FileTable:\n");
        for (var fileName : fileTable.toMap().keySet()) {
            sb.append(fileName.val()).append("\n");
        }

        return sb.toString();
    }
}
