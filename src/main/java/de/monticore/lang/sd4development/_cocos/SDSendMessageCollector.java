package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._visitor.SDBasisHandler;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;

import java.util.HashSet;
import java.util.Set;

public class SDSendMessageCollector implements SDBasisVisitor2, SDBasisHandler {

  Set<ASTSDSendMessage> result = new HashSet<>();
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
  public void visit(ASTSDSendMessage ast) {
    result.add(ast);
  }

  public Set<ASTSDSendMessage> getResult() {
    return result;
  }
}
