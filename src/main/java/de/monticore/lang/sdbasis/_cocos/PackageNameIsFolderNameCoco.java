package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.utils.Names;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if the package name of the sequence diagram artifact corresponds with
 * the actual location in the file system
 */
public class PackageNameIsFolderNameCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xB0018: " +
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
