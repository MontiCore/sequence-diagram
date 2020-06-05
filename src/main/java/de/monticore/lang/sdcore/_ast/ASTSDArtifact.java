/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._ast;

public class ASTSDArtifact extends ASTSDArtifactTOP {

  private String path;
  private String fileName;

  public ASTSDArtifact() {
    super();
  }

  public void setFileName(String path, String name) {
    this.path = path;
    this.fileName = name;
  }

  public String getPath() {
    return path;
  }

  public String getFileName() {
    return fileName;
  }

}
