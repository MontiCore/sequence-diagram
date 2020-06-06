package de.monticore.lang.sd4code._cocos;

import de.monticore.lang.sd4code._ast.ASTEndOfMethodInteraction;
import de.monticore.lang.sd4code._ast.ASTMethodInvocationAction;
import de.monticore.lang.sd4code._ast.ASTReturnAction;
import de.monticore.lang.sd4code._visitor.SD4CodeDelegatorVisitor;
import de.monticore.lang.sd4code._visitor.SD4CodeVisitor;
import de.monticore.lang.sdbase._ast.ASTOrdinaryInteraction;
import de.monticore.lang.sdbase._visitor.SDBaseVisitor;
import de.monticore.lang.sdcore._ast.ASTInteraction;
import de.monticore.lang.sdcore._ast.ASTInteractionEntity;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._cocos.SDCoreASTSDArtifactCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.Set;

public class ReturnOnlyAfterMethodCoco implements SDCoreASTSDArtifactCoCo {

  static final String MESSAGE = ReturnOnlyAfterMethodCoco.class.getSimpleName() + ": " +
          "Return %s occurs without previous call from %s to %s.";

  @Override
  public void check(ASTSDArtifact node) {
    ReturnOnlyAfterMethodCocoVisitor visitor = new ReturnOnlyAfterMethodCocoVisitor();
    node.accept(visitor);
  }

  private static final class ReturnOnlyAfterMethodCocoVisitor implements SD4CodeVisitor {
//    private final SDPrettyPrinter pp = new SDPrettyPrinter();

    private final Set<ASTInteraction> openMethodCalls = new HashSet<>();

    @Override
    public void visit(ASTOrdinaryInteraction node) {
      openMethodCalls.add(node);
    }

    @Override
    public void visit(ASTEndOfMethodInteraction node) {
      for (ASTInteraction interaction : openMethodCalls) {
        if (doInteractionsMatch(node, interaction)) {
          openMethodCalls.remove(interaction);
          return;
        }
      }
      String nodeAsString = node.toString();
      String targetAsString = node.isPresentTarget() ? node.getTarget().toString() : "";
      String sourceAsString = node.isPresentSource() ? node.getSource().toString() : "";
      Log.warn(String.format(MESSAGE, nodeAsString, targetAsString, sourceAsString),//pp.prettyPrint(node), pp.prettyPrint(node.getTarget(), pp.prettyPrint(node.getSource()))),
              node.get_SourcePositionStart());
    }

    private boolean doInteractionsMatch(ASTInteraction e1, ASTInteraction e2) {
      return _doInteractionsMatch(e1, e2) && _doInteractionsMatch(e2, e1);
    }

    private boolean _doInteractionsMatch(ASTInteraction e1, ASTInteraction e2) {
      if (!e1.isPresentSource() && !e2.isPresentTarget()) {
        return true;
      }
      if (e1.isPresentSource() && e2.isPresentTarget()) {
        return e1.getSource().deepEquals(e2.getTarget());
      }
      return false;
    }
  }
}
