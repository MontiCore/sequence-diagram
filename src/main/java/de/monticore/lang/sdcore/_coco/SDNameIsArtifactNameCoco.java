/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._cocos.SDCoreASTSDArtifactCoCo;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

/**
 * Verifies that the sd name is equal to the fileName
 */
public class SDNameIsArtifactNameCoco implements SDCoreASTSDArtifactCoCo {

  @Override
  public void check(ASTSDArtifact node) {

    String sdName = node.getSequenceDiagram().getName();
    String fileName = node.getFileName();
    String baseFileName = FilenameUtils.getBaseName(fileName);

    // Check if sdName == fileName without extension
    if (!sdName.equals(baseFileName)) {
      Log.warn(this.getClass().getSimpleName() + ": The sequence diagram name " + sdName
          + " does not match the artifact name " + baseFileName, node.get_SourcePositionStart());
    }

  }

}
