/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

public class SDNameIsArtifactNameCoco implements SDCoreASTSDArtifactCoCo {

  static final String MESSAGE = SDNameIsArtifactNameCoco.class.getSimpleName() + ": " +
        "The sequence diagram name %s does not match the artifact name %s";

  @Override
  public void check(ASTSDArtifact node) {

    String sdName = node.getSequenceDiagram().getName();
    String fileName = node.getFilePath().getFileName().toString();
    String baseFileName = FilenameUtils.getBaseName(fileName);

    if (!sdName.equals(baseFileName)) {
      Log.warn(String.format(MESSAGE , sdName, baseFileName), node.get_SourcePositionStart());
    }

  }

}
