/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._coco.core;

import com.google.common.collect.Sets;
import de.monticore.lang.sd.util.Duplicates;
import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTSequenceDiagram;
import de.monticore.lang.sdcore._cocos.SDCoreASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UniqueObjectNamingCoco implements SDCoreASTSequenceDiagramCoCo {

  @Override
  public void check(ASTSequenceDiagram sd) {
    List<ASTObject> objects = sd.getObjectList();
    if(hasDublicatedObjectNames(objects)) {
      List<String> duplicates = getDublicatedObjectNames(objects);
      duplicates.forEach(
              duplicate -> Log.error("Identifier " + duplicate + " ambiguous.")
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
  private boolean hasDublicatedObjectNames(List<ASTObject> sdObjects) {
    List<ASTObject> namedObjects = getNamedObjects(sdObjects);
    List<String> names = getNamesOfObject(namedObjects);
    Set<String> uniqueNames = Sets.newHashSet(names);
    return names.size() > uniqueNames.size();
  }
  private List<String> getDublicatedObjectNames(List<ASTObject> astsdObjects) {
    List<String> names = astsdObjects.stream().map(ASTObject::getName).collect(Collectors.toList());
    return Duplicates.inStringList(names);
  }


}
