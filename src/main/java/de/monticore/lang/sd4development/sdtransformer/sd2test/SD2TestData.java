/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.lang.sd4development.sdtransformer.SDData;

import java.util.Collection;

public class SD2TestData implements SDData {

  protected ASTCDCompilationUnit compilationUnit;

  protected Collection<ASTCDElement> classes;

  public SD2TestData(ASTCDCompilationUnit compilationUnit, Collection<ASTCDElement> classes) {
    this.compilationUnit = compilationUnit;
    this.classes = classes;
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
}
