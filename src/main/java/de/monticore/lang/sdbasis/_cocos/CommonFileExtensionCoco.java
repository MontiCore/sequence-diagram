package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

import static de.monticore.lang.sd4development._symboltable.SD4DevelopmentGlobalScope.FILE_EXTENSION;

public class CommonFileExtensionCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xS0014: " +
    "File extension is '%s', but should be " + FILE_EXTENSION;

  @Override
  public void check(ASTSDArtifact node) {
    String fileExtension = FilenameUtils.getExtension(node.getFilePath().toString());
    if (!FILE_EXTENSION.equals(fileExtension)) {
      Log.warn(String.format(MESSAGE, fileExtension));
    }
  }
}
