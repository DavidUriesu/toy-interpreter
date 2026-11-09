package interpreter.view;

import interpreter.model.exp.*;
import interpreter.model.stmt.*;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

public class Examples {

    public static IStmt example1() {
        // int v; v = 2; print(v)

        IStmt declaration = new VarDeclStmt("v", new IntType());

        IStmt assignment = new AssignStmt(
                "v", new ValueExp(new IntValue(2))
        );

        IStmt print = new PrintStmt(new VarExp("v"));

        return new CompStmt(
                declaration,
                new CompStmt(assignment, print)
        );
    }

    public static IStmt example2() {
        // int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)

        IStmt declareA = new VarDeclStmt("a", new IntType());
        IStmt declareB = new VarDeclStmt("b", new IntType());

        IExp multiplication = new ArithExp(
                ArithOp.MUL,
                new ValueExp(new IntValue(3)),
                new ValueExp(new IntValue(5))
        );

        IExp calculation = new ArithExp(
                ArithOp.ADD,
                new ValueExp(new IntValue(2)),
                multiplication
        );

        IStmt assignA = new AssignStmt("a", calculation);

        IStmt assignB = new AssignStmt(
                "b",
                new ArithExp(
                        ArithOp.ADD,
                        new VarExp("a"),
                        new ValueExp(new IntValue(1))
                )
        );

        IStmt print = new PrintStmt(new VarExp("b"));

        return new CompStmt(
                declareA,
                new CompStmt(
                        declareB,
                        new CompStmt(
                                assignA,
                                new CompStmt(assignB, print)
                        )
                )
        );
    }

    public static IStmt example3() {
        // bool a; int v; a = true;
        // if a then v = 2 else v = 3; print(v)

        IStmt declareA = new VarDeclStmt("a", new BoolType());
        IStmt declareV = new VarDeclStmt("v", new IntType());

        IStmt assignA = new AssignStmt(
                "a", new ValueExp(new BoolValue(true))
        );

        IStmt thenBranch = new AssignStmt(
                "v", new ValueExp(new IntValue(2))
        );

        IStmt elseBranch = new AssignStmt(
                "v", new ValueExp(new IntValue(3))
        );

        IStmt conditional = new IfStmt(
                new VarExp("a"), thenBranch, elseBranch
        );

        IStmt print = new PrintStmt(new VarExp("v"));

        return new CompStmt(
                declareA,
                new CompStmt(
                        declareV,
                        new CompStmt(
                                assignA,
                                new CompStmt(conditional, print)
                        )
                )
        );
    }

    public static IStmt fileExample(String fileName) {
        IStmt program = new CloseRFileStmt(new VarExp("varf"));

        program = new CompStmt(new PrintStmt(new VarExp("varc")), program);
        program = new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"), program);
        program = new CompStmt(new PrintStmt(new VarExp("varc")), program);
        program = new CompStmt(new ReadFileStmt(new VarExp("varf"), "varc"), program);
        program = new CompStmt(new VarDeclStmt("varc", new IntType()), program);
        program = new CompStmt(new OpenRFileStmt(new VarExp("varf")), program);
        program = new CompStmt(
                new AssignStmt("varf",
                        new ValueExp(new StringValue(fileName))), program);

        return new CompStmt(
                new VarDeclStmt("varf", new StringType()), program);
    }

    public static IStmt example4() {
        return fileExample("test.in");
    }

    public static IStmt example5() {
        IStmt conditional = new IfStmt(
                new RelationalExp(
                        RelOp.LT, new VarExp("a"), new VarExp("b")),
                new PrintStmt(new VarExp("a")),
                new PrintStmt(new VarExp("b"))
        );

        return new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a",
                                        new ValueExp(new IntValue(5))),
                                new CompStmt(
                                        new AssignStmt("b",
                                                new ValueExp(new IntValue(7))),
                                        conditional
                                )
                        )
                )
        );
    }
}