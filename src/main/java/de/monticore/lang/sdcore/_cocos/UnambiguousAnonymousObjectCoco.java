/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import com.google.common.collect.Sets;
import de.monticore.lang.util.Duplicates;
import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTSequenceDiagram;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnambiguousAnonymousObjectCoco implements SDCoreASTSequenceDiagramCoCo {

  static final String MESSAGE_ERROR_ANONYMOUS_OBJECT_AMBIGUOUS = UnambiguousAnonymousObjectCoco.class.getSimpleName() + ": "
          + "Anonymous object %s is ambiguous. No clear identifier.";

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public UnambiguousAnonymousObjectCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTObject> objects = sd.getObjectList();
    if (hasDublicatedAnonymousObject(objects)) {
      List<String> duplicates = getDublicatedAnonymousObject(objects);
      duplicates.forEach(
              duplicate -> Log.error(String.format(MESSAGE_ERROR_ANONYMOUS_OBJECT_AMBIGUOUS, duplicate))
      );
    }
  }

  private List<ASTObject> getAnonymousObjects(List<ASTObject> sdObjects) {
    return sdObjects.stream()
            .filter(object -> object.isPresentMCObjectType() && !object.isPresentName())
            .collect(Collectors.toList());
  }

  private boolean hasDublicatedAnonymousObject(List<ASTObject> sdObjects) {
    List<ASTObject> anonymousObject = getAnonymousObjects(sdObjects);
    List<String> anonymousTypeNames = getTypeOfObject(anonymousObject);
    Set<String> uniqueAnonymousTypeNames = Sets.newHashSet(anonymousTypeNames);
    return anonymousTypeNames.size() != uniqueAnonymousTypeNames.size();
  }

  private List<String> getDublicatedAnonymousObject(List<ASTObject> sdObjects) {
    List<ASTObject> anonymousObject = getAnonymousObjects(sdObjects);
    return new Duplicates<String>().apply(getTypeOfObject(anonymousObject));
  }

  private List<String> getTypeOfObject(List<ASTObject> objects) {
    return objects
            .stream()
            .map(object -> object.getMCObjectType().printType(prettyPrinter))
            .collect(Collectors.toList());
  }
}
