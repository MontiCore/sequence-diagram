/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.expressions.oclexpressions._symboltable.IOCLExpressionsGlobalScope;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ImportStatementsValidCoco implements SDASTSDArtifactCoCo {

  @Override
  public void check(ASTSDArtifact artifact) {

    String path = artifact.getPath();
    String fileName = artifact.getFileName();
    String root = path;

    // Package declaration present?
    if (artifact.isPresentMCQualifiedName()) {
      ASTMCQualifiedName packageDeclaration = artifact.getMCQualifiedName();
      String packageName = packageDeclaration.toString();
      PackageNameIsFolderNameCoco coco2 = new PackageNameIsFolderNameCoco();
      // Check if package name is valid
      if (!isSuffix(path, packageName)) {
        Log.error(this.getClass().getSimpleName() + ": Cannot resolve import statements. The package name "
            + packageName + " of " + path + "/" + fileName + " does not match its path on the file system",
            artifact.get_SourcePositionStart());
      }
      root = path.replaceAll(packageName.replaceAll("\\.", "/"), "");
    }


    // Check if for each import statement there exists a file
    for (ASTMCImportStatement imp : artifact.getMCImportStatementList()) {
      try {
        Stream<Path> pathStream = Files.list(Paths.get(root + imp.getQName()));
        pathStream.anyMatch(x -> x.toString() == imp.toString());
      } catch (IOException exception) {
        Log.error("")
      }

      Files.exists(Paths.get(root + imp.getQName()));
      File file = new File(root + imp.getQName(), imp.getFileName());
      if (!file.exists(Paths.get())) {
        Log.error(this.getClass().getSimpleName() + ": Cannot find file " + file.getAbsolutePath()
            + " referenced in import statement", imp.get_SourcePositionStart());
      }
    }
  }
  private boolean isSuffix(String path, String packageName) {
    String pathWithDots = path.replaceAll("/", ".");
    return pathWithDots.endsWith("." + packageName);
  }

}
