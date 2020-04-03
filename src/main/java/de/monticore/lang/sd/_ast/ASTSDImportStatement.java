/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._ast;

import de.monticore.types.types._ast.ASTQualifiedName;

import java.util.List;

public class ASTSDImportStatement extends ASTSDImportStatementTOP {

  public ASTSDImportStatement() {
    super();
  }

  public ASTSDImportStatement(ASTQualifiedName qualifiedName) {
    super(qualifiedName);
  }

  public String getPath() {
    String fqn = this.getQualifiedName().toString();
    fqn = fqn.substring(0, fqn.length() - getFileName().length());
    return fqn.replaceAll("\\.", "/");
  }

  public String getFileName() {
    List<String> parts = this.getQualifiedName().getPartList();
    String fileName = parts.get(parts.size() - 2) + "." + parts.get(parts.size() - 1);
    return fileName;
  }

}
