/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.modelloader.AstProvider;

import java.util.List;

public class SD4DevelopmentModelLoader extends SD4DevelopmentModelLoaderTOP {

  public SD4DevelopmentModelLoader(AstProvider<ASTSDArtifact> astProvider, SD4DevelopmentSymbolTableCreatorDelegator symbolTableCreator, String modelFileExtension) {
    super(astProvider, symbolTableCreator, modelFileExtension);
  }

  protected  void showWarningIfParsedModels (List<?> asts,String modelName) {
  }
}
