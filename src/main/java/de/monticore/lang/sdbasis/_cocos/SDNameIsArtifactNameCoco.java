/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

/**
 * Checks if the names of sequence diagrams are equal to the file names
 * of the artifacts containing the sequence diagrams.
 */
public class SDNameIsArtifactNameCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xB0021: " +
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
