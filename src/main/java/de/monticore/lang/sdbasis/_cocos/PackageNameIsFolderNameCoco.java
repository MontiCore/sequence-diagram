package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.utils.Names;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCoco implements SDCoreASTSDArtifactCoCo {

  static final String MESSAGE = PackageNameIsFolderNameCoco.class.getSimpleName() + ": " +
          "Package name '%s' does not correspond to the file path '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    if (node.isPresentPackageDeclaration()) {
      String packageName = node.getPackageDeclaration().getQName();
      if (!node.getFilePath().getParent().endsWith(Names.getPathFromPackage(packageName))) {
        Log.error(String.format(MESSAGE, packageName, node.getFilePath().toString()));
      }
    }
  }

}
