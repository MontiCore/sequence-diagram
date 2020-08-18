/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import com.google.common.collect.Sets;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Checks if every object has a unique name.
 */
public class UniqueObjectNamingCoco implements SDBasisASTSequenceDiagramCoCo {

  private static final String MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS = "0xB0024: "
          + "Identifier %s is ambiguous.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTSDObject> objects = sd.getSDObjectsList();
    if (hasDuplicatedObjectNames(objects)) {
      List<String> duplicates = getDuplicatedObjectNames(objects);
      duplicates.forEach(
              duplicate -> Log.error(String.format(MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS, duplicate))
      );
    }
  }

  private List<String> getNamesOfObject(List<ASTSDObject> sdObjects) {
    return sdObjects.stream().map(ASTSDObject::getName).collect(Collectors.toList());
  }

  private boolean hasDuplicatedObjectNames(List<ASTSDObject> sdObjects) {
    List<String> names = getNamesOfObject(sdObjects);
    Set<String> uniqueNames = Sets.newHashSet(names);
    return names.size() != uniqueNames.size();
  }

  private List<String> getDuplicatedObjectNames(List<ASTSDObject> sdObjects) {
    List<String> names = sdObjects.stream().map(ASTSDObject::getName).collect(Collectors.toList());
    return getDuplicates(names);
  }

  /**
   * Returns the list of all values contained in ts occurring at least twice in ts.
   *
   * @param ts some list
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
