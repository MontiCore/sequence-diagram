package de.monticore.lang.sd4development.sdgenerator.sd2cd;

import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.lang.sd4development.sdgenerator.SDData;

public class SD2CDData implements SDData {

  protected final ASTCDCompilationUnit compilationUnit;

  protected final ASTCDClass cdClass;

  public SD2CDData(ASTCDCompilationUnit compilationUnit, ASTCDClass cdClass) {
    this.compilationUnit = compilationUnit;
    this.cdClass = cdClass;
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
}
