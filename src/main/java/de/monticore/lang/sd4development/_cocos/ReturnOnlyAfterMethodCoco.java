package de.monticore.lang.sd4development._cocos;


import de.monticore.lang.sd4development._ast.ASTSDEndCall;
import de.monticore.lang.sd4development._ast.ASTSDReturn;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentInheritanceVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDInteraction;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDArtifactCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.Set;
/**
public class ReturnOnlyAfterMethodCoco implements SDBasisASTSDArtifactCoCo {

  static final String MESSAGE = ReturnOnlyAfterMethodCoco.class.getSimpleName() + ": " +
          "Return '%s' occurs without previous call from '%s' to '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    ReturnOnlyAfterMethodCocoVisitor visitor = new ReturnOnlyAfterMethodCocoVisitor();
    node.accept(visitor);
  }

  private static final class ReturnOnlyAfterMethodCocoVisitor implements SD4DevelopmentInheritanceVisitor {
//    private final SDPrettyPrinter pp = new SDPrettyPrinter();

    private final Set<ASTSDInteraction> openMethodCalls = new HashSet<>();

    private boolean isReturnInteraction = false;

    private SD4DevelopmentVisitor realThis = this;

    @Override
    public SD4DevelopmentVisitor getRealThis() {
      return realThis;
    }

    @Override
    public void setRealThis(SD4DevelopmentVisitor realThis) {
      this.realThis = realThis;
    }

    @Override
    public void visit(ASTSDSendMessage node) {
      openMethodCalls.add(node);
    }

    @Override
    public void visit(ASTSDReturn node) {
      isReturnInteraction = true;
    }

//    @Override
//    public void endVisit(ASTSDEndCall node) {
//      if (isReturnInteraction) {
//        isReturnInteraction = false;
//        for (ASTSDInteraction interaction : openMethodCalls) {
//          if (doInteractionsMatch(node, interaction)) {
//            openMethodCalls.remove(interaction);
//            return;
//          }
//        }
//        //TODO replace toString by prettyPrinter
//        String nodeAsString = node.toString();
//        String targetAsString = node.isPresentSDTarget() ? node.getSDTarget().toString() : "";
//        String sourceAsString = node.isPresentSDSource() ? node.getSDSource().toString() : "";
//        Log.warn(String.format(MESSAGE, nodeAsString, targetAsString, sourceAsString),//pp.prettyPrint(node), pp.prettyPrint(node.getTarget(), pp.prettyPrint(node.getSource()))),
//                node.get_SourcePositionStart());
//      }
//    }
//
//    private boolean doInteractionsMatch(ASTSDInteraction e1, ASTSDInteraction e2) {
//      if (e1.isPresentSDSource() != e2.isPresentSDTarget() || e1.isPresentSDTarget() != e2.isPresentSDSource()) {
//        return false;
//      }
//      boolean result = true;
//      if (e1.isPresentSDSource() && e2.isPresentSDTarget()) {
//        result = e1.getSDSource().deepEquals(e2.getSDTarget());
//      }
//      if (e1.isPresentSDTarget() && e2.isPresentSDSource()) {
//        result &= e1.getSDTarget().deepEquals(e2.getSDSource());
//      }
//      return result;
//    }
  }
}
**/
