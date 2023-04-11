package ast_elements;

import java.util.List;
import java.util.Map;

import SemanticAnalysis.SemanticAnalysisException;

public class TupleExpression extends CollectionExpressions {
    
    private List<Expression> values;
    private List<Type> types;
        
    
    public TupleExpression(List<Expression> values)
    {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = values.size();
        sb.append("(");
        for (int i = 0; i < size - 1; i++) {
            sb.append(values.get(i)).append(", ");
        }
        if (size >= 1) {
            sb.append(values.get(size - 1));
        }
        sb.append(")");
        return sb.toString();
    }

	@Override
	public Type analyzeAndGetType(Map<String, Type> variable_Map, Map<String, FunctionDeclaration> func_Map) throws SemanticAnalysisException {
		if (values.size() == 0) {
            return collectionType;
        }
        
        elementsType = values.get(0).analyzeAndGetType(variable_Map, func_Map);
        types.add(elementsType);
        for (int i = 0; i < values.size() - 1; i++) {
            types.add(values.get(i).analyzeAndGetType(variable_Map, func_Map));
            if (!elementsType.equals(values.get(i).analyzeAndGetType(variable_Map, func_Map))) {
                throw new SemanticAnalysisException("type of " + values.get(i) + " does not match with " + elementsType);
            }
        }
        return new TupleType(types);
	}

    @Override
    public int size() {
        return values.size();
    }
}
