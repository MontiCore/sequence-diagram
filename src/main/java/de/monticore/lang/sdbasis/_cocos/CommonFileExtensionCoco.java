/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

/**
 * Checks if an artifact containing an SD model has the common file ending ".sd" of
 * sequence diagram artifacts.
 */
public class CommonFileExtensionCoco implements SDBasisASTSDArtifactCoCo {

  private static final String FILE_EXTENSION = "sd";

  private static final String MESSAGE = "0xB0014: " + "File extension is '%s', but should be " + FILE_EXTENSION;

  @Override
  public void check(ASTSDArtifact node) {
    if (node.getFilePath() != null) {
      String fileExtension = FilenameUtils.getExtension(node.getFilePath().toString());
      if (!FILE_EXTENSION.equals(fileExtension)) {
        Log.warn(String.format(MESSAGE, fileExtension));
      }
    }
  }
}
