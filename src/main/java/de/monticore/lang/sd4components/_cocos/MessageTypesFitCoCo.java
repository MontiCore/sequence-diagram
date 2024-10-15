/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDMessage;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components.types.FullSD4ComponentsDeriver;
import de.monticore.lang.sdbasis._ast.ASTSDAction;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.monticore.symboltable.resolving.ResolvedSeveralEntriesForSymbolException;
import de.monticore.types.check.AbstractDerive;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types3.SymTypeRelations;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * This context-condition checks weather the type provided by a connector's
 * source port is compatible to the expected type of the connector's target
 * ports.
 * <p>
 * Implements [Hab16] R8: The target port in a connection has to be compatible
 * to the source port, i.e., the type of the target port is identical or a
 * supertype of the source port type. (p. 66, lst. 3.43)
 */
public class MessageTypesFitCoCo implements SDBasisASTSDSendMessageCoCo {

  public final AbstractDerive deriver;

  public MessageTypesFitCoCo() {
    this(new FullSD4ComponentsDeriver());
  }

  public MessageTypesFitCoCo(AbstractDerive deriver) {
    this.deriver = deriver;
  }

  @Override
  public void check(ASTSDSendMessage node) {
    // Collect types
    Optional<SymTypeExpression> targetType = Optional.empty();
    if (node.isPresentSDTarget() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(node.getSDTarget())) {
      ASTSDPort target = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(node.getSDTarget());
      if (target.isPresentNameSymbol() && target.getNameSymbol().isTypePresent()) {
        targetType = target.getNameSymbol().getType().getTypeOfPort(target.getPort());
      }
    }

    Optional<SymTypeExpression> sourceType = Optional.empty();
    if (node.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(node.getSDSource())
    ) {
      ASTSDPort source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(node.getSDSource());
      if (source.isPresentNameSymbol() && source.getNameSymbol().isTypePresent()) {
        sourceType = source.getNameSymbol().getType().getTypeOfPort(source.getPort());
      }
    }

    // Check source and target fit each other and actions fits source or target
    if (sourceType.isPresent() && targetType.isPresent()) {
      if (!SymTypeRelations.isCompatible(sourceType.get(), targetType.get())) {
        Log.error(String.format("0xB5000: Type mismatch, expected '%s' but provided '%s'", targetType.get().print(), sourceType.get().print()),
          node.get_SourcePositionStart(),
          node.get_SourcePositionEnd());
      }
      checkMessageFits(node.getSDAction(), sourceType.get());
    } else if (sourceType.isPresent()) {
      checkMessageFits(node.getSDAction(), sourceType.get());
    } else if (targetType.isPresent()) {
      checkMessageFits(node.getSDAction(), targetType.get());
    } else {
      Log.warn("Skipping CoCo MessageTypesFitCoCo");
    }
  }

  private void checkMessageFits(ASTSDAction sdAction, SymTypeExpression target) {
    if (!SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDMessage(sdAction))
      return;
    ASTSDMessage callAction = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDMessage(sdAction);
    try {
      TypeCheckResult result = deriver.deriveType(callAction.getExpression());
      if (!result.isPresentResult()) {
        return;
      }

      if (!SymTypeRelations.isCompatible(target, result.getResult())) {
        Log.error(String.format("0xB5001: Type mismatch, expected '%s' but provided '%s'", target.print(), result.getResult().print()), sdAction.get_SourcePositionStart(), sdAction.get_SourcePositionEnd());
      }
    } catch (ResolvedSeveralEntriesForSymbolException ignored) {
      Log.debug("Skipped CoCo MessageTypesFitCoCo: ResolvedSeveralEntriesForSymbolException", sdAction.get_SourcePositionStart(), "MessageTypesFitCoCo");
    }
  }
}
