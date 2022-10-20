package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4codebasis._ast.ASTCDParameter;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;

public class Parameter {
    public ASTCDParameter getParameterName() {
        return parameterName;
    }

    public ASTExpression getArgument() {
        return argument;
    }

    protected ASTCDParameter parameterName;
    protected ASTExpression argument;

    public Parameter(ASTCDParameter parameterName, ASTExpression argument) {
        this.parameterName = parameterName;
        this.argument = argument;
    }
}
