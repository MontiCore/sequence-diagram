package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._cocos.SDCoreASTSDArtifactCoCo;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCoco implements SDCoreASTSDArtifactCoCo {

  @Override
  public void check(ASTSDArtifact node) {
    // TODO: should be handled by symbol table
  }

}
