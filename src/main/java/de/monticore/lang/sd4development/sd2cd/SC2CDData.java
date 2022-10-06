package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;

import java.util.Collection;

public class SC2CDData {

  protected final ASTCDCompilationUnit compilationUnit;

  protected final ASTCDClass cdClass;

  public SC2CDData(ASTCDCompilationUnit compilationUnit, ASTCDClass cdClass) {
    this.compilationUnit = compilationUnit;
    this.cdClass = cdClass;
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
}
