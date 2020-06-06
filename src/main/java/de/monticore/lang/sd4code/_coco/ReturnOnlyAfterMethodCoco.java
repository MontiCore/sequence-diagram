package de.monticore.lang.sd4code._coco;

import de.monticore.lang.sd4code._ast.ASTMethodInvocationAction;
import de.monticore.lang.sd4code._ast.ASTReturnAction;
import de.monticore.lang.sd4code._visitor.SD4CodeVisitor;
import de.monticore.lang.sdcore._ast.ASTInteraction;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._cocos.SDCoreASTSDArtifactCoCo;

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

    private boolean isMethodCall = false;
    private boolean isReturnCall = false;

    @Override
    public void visit(ASTMethodInvocationAction node) {
      isMethodCall = true;
    }

    @Override
    public void visit(ASTReturnAction node) {
      isReturnCall = true;
    }

    @Override
    public void endVisit(ASTInteraction node) {
      if (isMethodCall) {
        isMethodCall = false;
        openMethodCalls.add(node);
      } else if (isReturnCall) {
        isReturnCall = false;
        for (ASTInteraction interaction : openMethodCalls) {
          if (interaction.getSource().deepEquals(node.getTarget()) && interaction.getTarget().deepEquals(node.getSource())) {
            openMethodCalls.remove(interaction);
            return;
          }
        }
//        Log.warn(String.format(MESSAGE, pp.prettyPrint(node), pp.prettyPrint(node.getTarget(), pp.prettyPrint(node.getSource()))),
//                node.get_SourcePositionStart());
      }
    }
  }
}
