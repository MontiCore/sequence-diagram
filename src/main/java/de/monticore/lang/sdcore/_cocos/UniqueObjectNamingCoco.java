/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import com.google.common.collect.Sets;
import de.monticore.lang.util.Duplicates;
import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniqueObjectNamingCoco implements SDCoreASTSequenceDiagramCoCo {

  static final String MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS = UniqueObjectNamingCoco.class.getSimpleName() + ": "
          +"Identifier %s is ambiguous.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTObject> objects = sd.getObjectList();
    if(hasDuplicatedObjectNames(objects)) {
      List<String> duplicates = getDuplicatedObjectNames(objects);
      duplicates.forEach(
              duplicate -> Log.error(String.format(MESSAGE_ERROR_IDENTIFIER_AMBIGUOUS, duplicate))
      );
    }
  }
  private List<ASTObject> getNamedObjects(List<ASTObject> sdObjects) {
    return sdObjects
            .stream()
            .filter(ASTObject::isPresentName)
            .collect(Collectors.toList());
  }
  private List<String> getNamesOfObject(List<ASTObject> sdObjects) {
    return sdObjects.stream().map(ASTObject::getName).collect(Collectors.toList());
  }
  private boolean hasDuplicatedObjectNames(List<ASTObject> sdObjects) {
    List<ASTObject> namedObjects = getNamedObjects(sdObjects);
    List<String> names = getNamesOfObject(namedObjects);
    Set<String> uniqueNames = Sets.newHashSet(names);
    return names.size() != uniqueNames.size();
  }
  private List<String> getDuplicatedObjectNames(List<ASTObject> astsdObjects) {
    List<String> names = astsdObjects.stream().filter(ASTObject::isPresentName).map(ASTObject::getName).collect(Collectors.toList());
    return Duplicates.inStringList(names);
  }


}
