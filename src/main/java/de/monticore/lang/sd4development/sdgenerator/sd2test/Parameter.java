package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4codebasis._ast.ASTCDParameter;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;

public class Parameter {
    public VariableSymbol getParameterName() {
        return parameterName;
    }

    public ASTExpression getArgument() {
        return argument;
    }

    protected VariableSymbol parameterName;
    protected ASTExpression argument;

    public Parameter(VariableSymbol parameterName, ASTExpression argument) {
        this.parameterName = parameterName;
        this.argument = argument;
    }
}
