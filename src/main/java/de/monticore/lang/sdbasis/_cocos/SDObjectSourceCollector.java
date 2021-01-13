package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObjectSource;
import de.monticore.lang.sdbasis._visitor.SDBasisHandler;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;

import java.util.HashSet;
import java.util.Set;

public class SDObjectSourceCollector implements SDBasisVisitor2, SDBasisHandler {

  Set<ASTSDObjectSource> result = new HashSet<>();
  SDBasisTraverser traverser;

  @Override
  public SDBasisTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SDBasisTraverser traverser) {
    this.traverser = traverser;
  }

  @Override
  public void visit(ASTSDObjectSource ast) {
    result.add(ast);
  }

  public Set<ASTSDObjectSource> getResult() {
    return result;
  }
}
