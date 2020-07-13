package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDEndCall;
import de.monticore.lang.sd4development._ast.ASTSDReturn;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentInheritanceVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDArtifactCoCo;
import de.monticore.lang.util.SourceAndTargetEquals;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Checks if a return call only appears after a method call has been invoked before
 */
public class ReturnOnlyAfterMethodCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xS0013: " +
          "Return call '%s' occurs without previous call from '%s' to '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    ReturnOnlyAfterMethodCocoVisitor visitor = new ReturnOnlyAfterMethodCocoVisitor();
    node.accept(visitor);
  }

  /**
   * This visitor traverses the AST of the sequence diagram, and collects any SendCall invocation.
   * If there exists a return end call, it is checked if any open SendCall matches with the given EndCall.
   * If there does not exists such a SendCall, an error will be produced.
   */
  private static final class ReturnOnlyAfterMethodCocoVisitor implements SD4DevelopmentInheritanceVisitor {
    private final SD4DevelopmentDelegatorPrettyPrinter pp = new SD4DevelopmentDelegatorPrettyPrinter();

    private final Set<ASTSDSendMessage> openMethodCalls = new HashSet<>();

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

    @Override
    public void endVisit(ASTSDEndCall endCall) {
      if (isReturnInteraction) {
        isReturnInteraction = false;
        for (ASTSDSendMessage sendMessage : openMethodCalls) {
          if (doInteractionsMatch(sendMessage, endCall)) {
            openMethodCalls.remove(sendMessage);
            return;
          }
        }
        String nodeAsString = pp.prettyPrint(endCall).trim();
        String targetAsString = endCall.isPresentSDTarget() ? pp.prettyPrint(endCall.getSDTarget()) : "<empty>";
        String sourceAsString = endCall.isPresentSDSource() ? pp.prettyPrint(endCall.getSDSource()) : "<empty>";
        Log.warn(String.format(MESSAGE, nodeAsString, targetAsString, sourceAsString),
                endCall.get_SourcePositionStart());
      }
    }

    private boolean doInteractionsMatch(ASTSDSendMessage e1, ASTSDEndCall e2) {
      // source of SendMessage is present iff target of EndCall is present
      if (e1.isPresentSDSource() != e2.isPresentSDTarget() || e1.isPresentSDTarget() != e2.isPresentSDSource()) {
        return false;
      }
      SourceAndTargetEquals equalizer = new SourceAndTargetEquals();
      boolean result = true;
      if (e1.isPresentSDSource()) {
        result = equalizer.equals(e1.getSDSource(), e2.getSDTarget());
      }
      if (e1.isPresentSDTarget()) {
        result &= equalizer.equals(e2.getSDSource(), e1.getSDTarget());
      }
      return result;
    }
  }
}
