package ast_elements;

import java.util.*;

import SemanticAnalysis.SemanticAnalysisException;

public class WhileLoop extends Statement {

    private Expression cond;
    private List<Statement> body;

    public WhileLoop(Expression cond, List<Statement> body) {
        this.cond = cond;
        this.body = body;
        System.out.println("CONDITION === " + cond.toString());
    }

    public StringBuilder toString(int indent) {
        String ind = IndentUtil.indentStr(indent);
        StringBuilder sb = new StringBuilder();
        sb.append(ind).append("while ").append(this.cond.toString()).append(":\n");
        for (Statement stmt : this.body) {
            sb.append(stmt.toString(indent + 1));
        }
        return sb;
    }

    @Override
    public void analyze(Map<String, Type> variable_Map, Map<String, FunctionDeclaration> func_Map) throws SemanticAnalysisException {
        Map<String, Type> localVar_Map = new HashMap<String, Type>(variable_Map);
        Map<String, FunctionDeclaration> localFun_Map = new HashMap<String, FunctionDeclaration>(func_Map);
        if (!(cond instanceof BooleanExpression)) {
            throw new SemanticAnalysisException("It is not boolean");
        }

        for (Statement stmt : body) {
            stmt.analyze(localVar_Map, localFun_Map);
        }
        localVar_Map = null;
        localFun_Map = null;
    }
}
