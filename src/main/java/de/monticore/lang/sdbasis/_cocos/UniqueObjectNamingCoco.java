/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import com.google.common.collect.Sets;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.util.Duplicates;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniqueObjectNamingCoco implements SDBasisASTSequenceDiagramCoCo {

  static final String MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS = UniqueObjectNamingCoco.class.getSimpleName() + ": "
          + "Identifier %s is ambiguous.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTSDObject> objects = sd.getSDObjectList();
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
    return new Duplicates<String>().apply(names);
  }


}
