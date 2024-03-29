/* generated from model SD4Development */
/* generated by template core.Class*/

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;



public class SD4DevelopmentSymbols2Json extends SD4DevelopmentSymbols2JsonTOP {
  public void visit(ISD4DevelopmentScope node) {
    super.visit(node);
    for (ISD4DevelopmentScope scope : node.getSubScopes()) {
      if (!scope.isPresentSpanningSymbol() || !scope.isExportingSymbols()) {
        getTraverser().addTraversedElement(scope);
      }
    }
  }

  public void visit(ISD4DevelopmentArtifactScope node) {
    super.visit(node);
    for (ISD4DevelopmentScope scope : node.getSubScopes()) {
      if (!scope.isPresentSpanningSymbol() || !scope.isExportingSymbols()) {
        getTraverser().addTraversedElement(scope);
      }
    }
  }

}
