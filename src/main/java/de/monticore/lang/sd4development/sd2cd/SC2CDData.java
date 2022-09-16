package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;

import java.util.Collection;

public class SC2CDData {

  protected final ASTCDCompilationUnit compilationUnit;

  protected final ASTCDClass cdClass;

  protected final Collection<ASTCDClass> classes;

  public SC2CDData(ASTCDCompilationUnit compilationUnit, ASTCDClass cdClass, Collection<ASTCDClass> classes) {
    this.compilationUnit = compilationUnit;
    this.cdClass = cdClass;
    this.classes = classes;
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
}
