package de.monticore.lang.sd4development.prettyprint;

import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.umlstereotype._ast.ASTStereotype;

import java.util.List;
import java.util.stream.Collectors;

public class PrettyPrinter implements SDBasisVisitor {
    private String result = "";

    private int indention = 0;

    private String indent = "";


    private final MCBasicTypesPrettyPrinter prettyPrinter;

    public PrettyPrinter() {
        this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
    }


    public String getResult() {
        return this.result;
    }

    @Override
    public void visit(ASTSDArtifact node) {
        println("package " + node.getPackageDeclaration().getQName() + ";");
    }
    @Override
    public void visit(ASTMCImportStatement node) {
    }
    @Override
    public void visit(ASTSequenceDiagram node) {
        println(printSterotypeIP(node) + " " + printModifierIP(node) + " sequencediagram " + node.getName() + " {");
        indent();
    }
    @Override
    public void visit(ASTSDObject node) {
        println(printSterotypeIP(node) + " "
                + printModifierIP(node) + " "
                + node.getName()
                + printTypeOfObjectIP(node));
    }
    private String printTypeOfObjectIP(ASTSDObject node) {
       if(node.isPresentMCObjectType()) {
          return ": " + getMCObjectTypeName(node.getMCObjectType());
       }
       return blankPrint();
    }
    private String blankPrint() {
        return " ";
    }
    private String emptyPrint() {
        return "";
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

    private String printSterotypeIP(ASTSDObject node) {
        if(node.isPresentStereotype()) {
            return getSterotypeName(node.getStereotype());
        }
        return emptyPrint();
    }
    private String getSterotypeName(ASTStereotype node) {
        return node.toString();
    }
    private String printModifierIP(ASTSDObject node) {
        List<String> modNames = getModifierNames(node.getSDModifierList());
        return printListAsString(modNames);
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


    private void println(String s) {
        result += (indent + s + "\n");
        indent = "";
        calcIndention();
    }
    private void calcIndention() {
        indent = "";
        for (int i = 0; i < indention; i++) {
            indent += "  ";
        }
    }

    private void indent() {
        indention++;
        calcIndention();
    }

    private void unindent() {
        indention--;
        calcIndention();
    }
}
