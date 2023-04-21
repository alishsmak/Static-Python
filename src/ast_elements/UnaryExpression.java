package ast_elements;

import java.util.Map;

import Executor.ExecutionException;
import SemanticAnalysis.SemanticAnalysisException;

public class UnaryExpression extends Expression {

    private String op;
    private Expression ex;

    
    public UnaryExpression(String op, Expression ex) {
        this.op = op;
        this.ex = ex;
    }


    @Override
    public String toString() {
        return op + " " + ex;
    }
    
    @Override
    public void analyze(Map<String, Type> variable_Map, Map<String, FunctionDeclaration> func_Map, Type expectedType)
            throws SemanticAnalysisException {
        if (op.equals("not")) {
            ex.analyze(variable_Map, func_Map, BooleanExpression.TYPE);
        } 
        else if (op.equals("-")) {
            Type type = ex.analyzeAndGetType(variable_Map, func_Map);
            if (!type.equals(NumberExpression.TYPE) && !type.equals(FloatExpression.TYPE)) {
                throw new SemanticAnalysisException("unary operation '-' does not support for " + ex);
            }
        }
        ex.analyze(variable_Map, func_Map, expectedType);
    }

    @Override
    public Object evaluate(Map<String, Object> variable_Map, Map<String, FunctionDeclaration> func_Map)
            throws ExecutionException {
        if (op.equals("not")) {
            return !(Boolean)ex.evaluate(variable_Map, func_Map);
        }
        if (op.equals("-")) {
            Object obj = ex.evaluate(variable_Map, func_Map);
            if (obj instanceof Integer) {
                return - (Integer)obj;
            }
            if (obj instanceof Float) {
                return - (Float)obj;
            }
        }
        return null;
    }
}
