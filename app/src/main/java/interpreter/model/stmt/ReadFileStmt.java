package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symbols = state.getSymTable();

        if (!symbols.isDefined(varName)) {
            throw new StatementException(
                    "readFile: variable is not declared: " + varName);
        }
        if (!(symbols.lookup(varName) instanceof IntValue)) {
            throw new StatementException("readFile: variable is not an integer");
        }

        if (!(exp.eval(symbols) instanceof StringValue fileName)) {
            throw new StatementException("readFile: filename is not a string");
        }

        var files = state.getFileTable();
        if (!files.isDefined(fileName)) {
            throw new StatementException("readFile: file is not open");
        }

        try {
            String line = files.lookup(fileName).readLine();
            int number;

            if (line == null) {
                number = 0;
            } else {
                number = Integer.parseInt(line.strip());
            }
            symbols.update(varName, new IntValue(number));
        } catch(NumberFormatException e) {
            throw new StatementException("readFile: line is not an integer");
        } catch(IOException e) {
            throw new StatementException(
                    "readFile: cannot read " + fileName + ": " + e.getMessage());
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}
