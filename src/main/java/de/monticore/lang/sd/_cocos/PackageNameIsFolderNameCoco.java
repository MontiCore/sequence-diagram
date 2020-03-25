/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDPackageDeclaration;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCoco implements SDASTSDArtifactCoCo {

  @Override
  public void check(ASTSDArtifact node) {

    String path = node.getPath();
    String file = node.getFileName();

    if (node.isPresentPackageDeclaration()) {
      ASTSDPackageDeclaration packageDeclaration = node.getPackageDeclaration();
      String packageName = packageDeclaration.getQualifiedName().toString();
      // Check if package is suffix of path
      if (!isSuffix(path, packageName)) {
        Log.warn(this.getClass().getSimpleName() + ": The package name " + packageName + " of " + path + file
            + " does not match its path on the file system");
      }
    }
  }

  public boolean isSuffix(String path, String packageName) {
    String pathWithDots = path.replaceAll("/", "\\.");
    return pathWithDots.endsWith("." + packageName);
  }

}
