/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._cocos.SDCoreASTSDArtifactCoCo;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;

public class SDNameIsArtifactNameCoco implements SDCoreASTSDArtifactCoCo {

  static final String MESSAGE_WARNING_UPPER_CASE = "%s The sequence diagram name %s"
          + " does not match the artifact name %s";

  @Override
  public void check(ASTSDArtifact node) {

    String sdName = node.getSequenceDiagram().getName();
    String fileName = node.getFileName();
    String baseFileName = FilenameUtils.getBaseName(fileName);

    if (!sdName.equals(baseFileName)) {
      Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, this.getClass().getSimpleName() , sdName, baseFileName), node.get_SourcePositionStart());
    }

  }

}
