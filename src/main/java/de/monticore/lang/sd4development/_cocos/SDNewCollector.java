package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentHandler;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;

import java.util.HashSet;
import java.util.Set;

public class SDNewCollector implements SD4DevelopmentVisitor2, SD4DevelopmentHandler {

  Set<ASTSDNew> result = new HashSet<>();
  SD4DevelopmentTraverser traverser;

  @Override
  public SD4DevelopmentTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SD4DevelopmentTraverser traverser) {
    this.traverser = traverser;
  }

  @Override
  public void visit(ASTSDNew ast) {
    result.add(ast);
  }

  public Set<ASTSDNew> getResult() {
    return result;
  }
}
