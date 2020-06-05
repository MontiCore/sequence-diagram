/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._coco.core;

import com.google.common.collect.Sets;
import de.monticore.lang.sd.util.Duplicates;
import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTSequenceDiagram;
import de.monticore.lang.sdcore._cocos.SDCoreASTSequenceDiagramCoCo;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UnambiguousAnonymousObjectCoco implements SDCoreASTSequenceDiagramCoCo {

  private final MCBasicTypesPrettyPrinter prettyPrinter;
  public UnambiguousAnonymousObjectCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTObject> objects = sd.getObjectList();
    if(hasDublicatedAnonymousObject(objects)) {
      List<String> duplicates = getDublicatedAnonymousObject(objects);
      duplicates.forEach(
              duplicate -> Log.error("Anonymous object " + duplicate + " is ambiguous. No clear identifier.")
      );
    }
  }
  private List<ASTObject> getAnonymousObjects(List<ASTObject> sdObjects) {
    return sdObjects.stream()
            .filter(object -> object.isPresentMCObjectType() && !object.isPresentName())
            .collect(Collectors.toList());
  }
  private List<String> getTypeOfObject(List<ASTObject> objects) {
    return objects
            .stream()
            .map(object -> object.getMCObjectType().printType(prettyPrinter))
            .collect(Collectors.toList());
  }
  private boolean hasDublicatedAnonymousObject(List<ASTObject> sdObjects) {
    List<ASTObject> anonymousObject = getAnonymousObjects(sdObjects);
    List<String> anonymousTypeNames = getTypeOfObject(anonymousObject);
    Set<String> uniqueAnonymousTypeNames = Sets.newHashSet(anonymousTypeNames);
    return anonymousTypeNames.size() > uniqueAnonymousTypeNames.size();
  }
  private List<String> getDublicatedAnonymousObject(List<ASTObject> sdObjects) {
    List<ASTObject> anonymousObject = getAnonymousObjects(sdObjects);
    List<String> anonymousObjectNames = getTypeOfObject(anonymousObject);
    return Duplicates.inStringList(anonymousObjectNames);
  }

}
