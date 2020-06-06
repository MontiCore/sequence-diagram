/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._ast;

import java.nio.file.Path;

public class ASTSDArtifact extends ASTSDArtifactTOP {

  private Path filePath;

  public ASTSDArtifact() {
    super();
  }

  public Path getFilePath() {
    return filePath;
  }

  public void setFilePath(Path filePath) {
    this.filePath = filePath;
  }
}
