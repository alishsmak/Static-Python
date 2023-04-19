package ast_elements;

import java.util.List;
import java.util.Map;

import SemanticAnalysis.SemanticAnalysisException;

public class FunctionCall extends Expression {

    private String func_name;
    private List<Expression> ex_list;
    private Expression obj;

    private Type type;
    public FunctionCall(String func_name, List<Expression> ex_list, Expression obj) {
        this.func_name = func_name;
        this.ex_list = ex_list;
        this.obj = obj;
    }

    public String getFunc_name() {
        return func_name;
    }

    public StringBuilder toString(int indent) {
        String ind = IndentUtil.indentStr(indent);
        StringBuilder sb = new StringBuilder();
        sb.append(ind);
        if (this.obj == null)
            sb.append(this.func_name + "(");
        else
            sb.append(this.obj + "." + this.func_name + "(");
        int size = ex_list.size();
        for (int i = 0; i < size - 1; i++)
            sb.append(ex_list.get(i)).append(", ");
        if (size >= 1)
            sb.append(ex_list.get(size - 1) + ")");
        else
            sb.append(")");
        // sb.append("\n>>>> ex_list.size(): " + this.ex_list.size() + " <<<<");
        return sb;
    }

    @Override
    public String toString() {
        return toString(0).toString();
    }

    @Override
    public void analyze(Map<String, Type> variable_Map, Map<String, FunctionDeclaration> func_Map, Type expectedType)
            throws SemanticAnalysisException {
        type = expectedType;
        if (expectedType != null && expectedType != func_Map.get(func_name).getReturn_Type())
            throw new SemanticAnalysisException("expected type and return type don't match");

        if (this.obj == null) {
            if (!func_Map.containsKey(func_name))
                throw new SemanticAnalysisException("function doesn't exist");

            if (ex_list.size() != func_Map.get(func_name).getParam_list().size())
                throw new SemanticAnalysisException("number of parameters doesn't match");

            for (int i = 0; i < ex_list.size(); i++)
                ex_list.get(i).analyze(variable_Map, func_Map,
                        func_Map.get(func_name).getParam_list().get(i).getType());
        } else {
            Type obj_Type = this.obj.analyzeAndGetType(variable_Map, func_Map);
            // if (obj_Type instanceof CollectionType) {
            //     CollectionType obj_col_Type = (CollectionType) obj_Type;
                
            // }
            
            if (obj_Type instanceof ListType) {
                ListType obj_list_type = (ListType)obj_Type;
                if (this.func_name == "copy" || this.func_name == "clear" || this.func_name == "reverse" || this.func_name == "sort") {
                    if (this.ex_list.size() != 0) {
                        throw new SemanticAnalysisException(this.func_name + " does not have arguments");
                    }
                    return;
                }

                if (func_name == "append" || this.func_name == "remove" || this.func_name == "count") {
                    if (ex_list.size() != 1) {
                        throw new SemanticAnalysisException(this.func_name + " cannot have less or more than one argument");
                    }
                    this.ex_list.get(0).analyze(variable_Map, func_Map, obj_list_type.getElements_Type());
                }
                
                // for (int i = 0; i < ex_list.size(); i++) {
                //     ex_list.get(i).analyze(variable_Map, func_Map, obj_col_Type.getElements_Type());
                // }

                if (this.func_name == "index") {
                    if (this.ex_list.size() == 0) {
                        throw new SemanticAnalysisException(this.func_name + " must have at least one argument");
                    } else if (this.ex_list.size() > 3) {
                        throw new SemanticAnalysisException(this.func_name + " cannot have more that three arguments");
                    }
                    this.ex_list.get(0).analyze(variable_Map, func_Map, obj_list_type.getElements_Type());
                    for (int i=1; this.ex_list.get(i) != null; i++) {
                        this.ex_list.get(i).analyze(variable_Map, func_Map, NumberExpression.getType());
                    }
                }
            }

            if (obj_Type instanceof SetType) {
                if (this.func_name == "copy" || this.func_name == "clear" || this.func_name == "sort") {
                    if (this.ex_list.size() != 0) {
                        throw new SemanticAnalysisException(this.func_name + " does not have arguments");
                    }
                    return;
                }
            }

            if (obj_Type instanceof VariableType) {
                VariableType obj_var_Type = (VariableType) obj_Type;
                if (obj_var_Type.getType() == "int") {

                } else if (obj_var_Type.getType() == "float") {

                } else if (obj_var_Type.getType() == "str") {

                } else if (obj_var_Type.getType() == "bool") {

                }
            }
        }
    }

    @Override
    public Type analyzeAndGetType(Map<String, Type> variable_Map, Map<String, FunctionDeclaration> func_Map)
            throws SemanticAnalysisException {
        return type;
    }
}
