package de.monticore.lang.sd4development.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.lang.sd4development._ast.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesVisitor;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.umlstereotype._ast.ASTStereotype;

import java.util.List;
import java.util.stream.Collectors;

public class PrettyPrinter extends MCCommonLiteralsPrettyPrinter  implements SD4DevelopmentVisitor, MCBasicTypesVisitor {

    private SD4DevelopmentVisitor realThis = this;
    private String result = "";

    IndentPrinter indentPrinter;

    private final MCBasicTypesPrettyPrinter prettyPrinter;

    public PrettyPrinter(IndentPrinter printer) {
        super(printer);
        this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
    }
    @Override
    public void setRealThis(SD4DevelopmentVisitor realThis) {
        this.realThis = realThis;
    }

    @Override
    public SD4DevelopmentVisitor getRealThis() {
        return realThis;
    }

    public String getResult() {
        return this.result;
    }

    @Override
    public void visit(ASTSDArtifact node) {
        getPrinter().println("package " + node.getPackageDeclaration().getQName() + ";");
    }

    @Override
    public void endVisit(ASTSequenceDiagram node) {
        getPrinter().unindent();
        getPrinter().println("}");
    }
    @Override
    public void visit(ASTMCImportStatement node) {
    }
    //TODO: das hier funktioniert leider nicht ....hab ich jetzt anders gelöst...
    @Override
    public void visit(ASTMCObjectType node) {

    }
    @Override
    public void visit(ASTSequenceDiagram node) {
        getPrinter().println(printSterotypeIP(node) + " " + printModifierIP(node) + " sequencediagram " + node.getName() + " {");
       // getPrinter().indent();
    }
    @Override
    public void visit(ASTSDObject node) {
        getPrinter().println(printSterotypeIP(node) + " "
                + printModifierIP(node) + " "
                + node.getName()
                + printTypeOfObjectIP(node)
                + ";");
    }

    @Override
    public void traverse(ASTSDSendMessage node) {
        if (node.isPresentSDSource()) {
            node.getSDSource().accept(getRealThis());
        }
        getPrinter().print(" -> ");
        if (node.isPresentSDTarget()) {
            node.getSDTarget().accept(getRealThis());
        }
        getPrinter().print(" : ");
        if (null != node.getSDAction()) {
            node.getSDAction().accept(getRealThis());
        }
        if (node.isPresentSDActivityBar()) {
            node.getSDActivityBar().accept(getRealThis());
        } else {
            getPrinter().print(";");
        }
        getPrinter().println();
    }
    @Override
    public void traverse(ASTSDNew node) {
        if (node.isPresentSDSource()) {
            node.getSDSource().accept(getRealThis());
        }
        getPrinter().print(" -> ");
        if (null != node.getDeclarationType()) {
            node.getDeclarationType().accept(getRealThis());
        }
        getPrinter().print(getMCObjectTypeName(node.getDeclarationType()) + " " + node.getName());
        if (null != node.getInitializationType()) {
            node.getInitializationType().accept(getRealThis());
        }
        getPrinter().print(" = new " + getMCObjectTypeName(node.getInitializationType()));
        if (null != node.getArguments()) {
            node.getArguments().accept(getRealThis());
        }
        if (node.isPresentSDActivityBar()) {
            node.getSDActivityBar().accept(getRealThis());
        } else {
            getPrinter().print(";");
        }
        getPrinter().println();
    }

    @Override
    public void visit(ASTSDActivityBar node) {
        getPrinter().println(" {");
        getPrinter().indent();
    }

    @Override
    public void endVisit(ASTSDActivityBar node) {
        getPrinter().print(" }");
        getPrinter().unindent();
    }

    @Override
    public void visit(ASTSDCall node) {
        String sdCallPrint = "";
        sdCallPrint += node.isTrigger() ? "trigger " : "";
        sdCallPrint += node.isStatic() ? "static " : "";
        sdCallPrint += node.getName();
        getPrinter().print(sdCallPrint);
    }

    @Override
    public void visit(ASTArguments node) {
        getPrinter().print("(");
    }
    public String prettyprint(ASTSDArtifact a) {
        a.accept(getRealThis());
        return printer.getContent();
    }

    @Override
    public void endVisit(ASTArguments node) {
        getPrinter().print(")");
    }

    @Override
    public void visit(ASTNameExpression node) {
        getPrinter().print(node.getName());
    }
    @Override
    public void visit(ASTSDIncompleteExpression node) {
        getPrinter().print("...");
    }

    public void visit(ASTSDObjectSource node) {
        getPrinter().print(node.getName());
    }
    public void visit(ASTSDObjectTarget node) {
        getPrinter().print(node.getName());
    }
    public void visit(ASTSDClass node) {
        getPrinter().print("class " + getMCObjectTypeName(node.getMCObjectType()));
    }
    private String printTypeOfObjectIP(ASTSDObject node) {
       if(node.isPresentMCObjectType()) {
          return ": " + getMCObjectTypeName(node.getMCObjectType());
       }
       return blankPrint();
    }
    private String getMCObjectTypeName(ASTMCObjectType node) {
        return node.printType(prettyPrinter);
    }
    private String printSterotypeIP(ASTSequenceDiagram node) {
        if(node.isPresentStereotype()) {
             return getSterotypeName(node.getStereotype());
        }
        return emptyPrint();
    }
    //TODO: Richtige Sterotype ausgabe fehlt - print ..
    private String printSterotypeIP(ASTSDObject node) {
        if(node.isPresentStereotype()) {
            return getSterotypeName(node.getStereotype());
        }
        return emptyPrint();
    }
    private String getSterotypeName(ASTStereotype node) {
        return node.toString();
    }
    //TODO: Richtige Modifier ausgabe fehlt - print ..
    private String printModifierIP(ASTSDObject node) {
        List<String> modNames = getModifierNames(node.getSDModifierList());
        return printListAsString(modNames);
    }

    @Override
    public void traverse(ASTSDEndCall node) {
        if (node.isPresentSDTarget()) {
            node.getSDTarget().accept(getRealThis());
        }
        if (null != node.getSDEndCallArrow()) {
            node.getSDEndCallArrow().accept(getRealThis());
        }
        getPrinter().print(" <- ");

        if (node.isPresentSDSource()) {
            node.getSDSource().accept(getRealThis());
        }
        getPrinter().print(" : ");
        if (null != node.getSDAction()) {
            node.getSDAction().accept(getRealThis());
        }
        getPrinter().println(";");

    }

    private String printModifierIP(ASTSequenceDiagram node) {
        List<String> modNames = getModifierNames(node.getSDModifierList());
        return printListAsString(modNames);
    }
    private List<String> getModifierNames(List<ASTSDModifier> modifiers) {
        return modifiers.stream().map(Object::toString).collect(Collectors.toList());
    }
    private String printListAsString(List<String> names) {
        return String.join(" ", names);
    }

    private String blankPrint() {
        return " ";
    }
    private String emptyPrint() {
        return "";
    }


}
