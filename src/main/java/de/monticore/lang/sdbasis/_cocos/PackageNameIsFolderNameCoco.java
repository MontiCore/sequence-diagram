/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if the package names of sequence diagram artifacts corresponds to
 * their actual locations in the file system.
 */
public class PackageNameIsFolderNameCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xB0018: " +
          "Package name '%s' does not correspond to the file path '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    if (node.isPresentPackageDeclaration()) {
      String packageName = node.getPackageDeclaration().getQName();
      if (node.getFilePath() != null && !node.getFilePath().getParent().endsWith(Names.getPathFromPackage(packageName))) {
        Log.error(String.format(MESSAGE, packageName, node.getFilePath().toString()));
      }
    }
  }

}
