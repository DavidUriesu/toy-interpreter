package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName)) {
            throw new StatementException("closeRFile: filename is not a string");
        }

        var files = state.getFileTable();
        if(!files.isDefined(fileName)) {
            throw new StatementException("closeRFile: file is not open");
        }

        try {
            files.lookup(fileName).close();
        } catch(IOException e) {
            throw new StatementException(
                    "closeRFile: cannot close " + fileName + ": " + e.getMessage());
        }

        files.remove(fileName);
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
