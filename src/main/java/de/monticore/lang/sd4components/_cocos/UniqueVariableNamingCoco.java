/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4components._cocos;

import com.google.common.collect.Sets;
import de.monticore.lang.sd4components._symboltable.ISD4ComponentsScope;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.compsymbols._symboltable.SubcomponentSymbolTOP;
import de.se_rwth.commons.logging.Log;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks if every variable has a unique name.
 */
public class UniqueVariableNamingCoco implements SDBasisASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS = "0xB5007: "
    + "Identifier %s is ambiguous.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<String> names = sd.getSDBody().getSpannedScope().getLocalVariableSymbols().stream().map(VariableSymbol::getName).collect(Collectors.toList());
    names.addAll(((ISD4ComponentsScope) sd.getEnclosingScope()).getLocalSubcomponentSymbols().stream().map(SubcomponentSymbolTOP::getName).collect(Collectors.toList()));
    if (hasDuplicated(names)) {
      List<String> duplicates = getDuplicates(names);
      duplicates.forEach(
        duplicate -> Log.error(String.format(MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS, duplicate))
      );
    }
  }

  private boolean hasDuplicated(List<String> names) {
    Set<String> uniqueNames = Sets.newHashSet(names);
    return names.size() != uniqueNames.size();
  }

  /**
   * Returns the list of all values contained in ts occurring at least twice in ts.
   *
   * @param ts  some list
   * @param <T> the type of the elements in the list
   * @return the list containing the duplicated values.
   */
  private <T> List<T> getDuplicates(List<T> ts) {
    return ts.stream()
      .filter(e -> Collections.frequency(ts, e) > 1)
      .distinct()
      .collect(Collectors.toList());
  }
}
