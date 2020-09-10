/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelPath;

public class SD4DevelopmentGlobalScope extends SD4DevelopmentGlobalScopeTOP {

  public static final String FILE_EXTENSION = "sd";

  private SD4DevelopmentGlobalScope realThis;

  public SD4DevelopmentGlobalScope(ModelPath modelPath, String modelFileExtension) {
    super(modelPath, modelFileExtension);
    this.realThis = this;
  }

  @Override
  public SD4DevelopmentGlobalScope getRealThis() {
    return realThis;
  }
}
