/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelPath;
import de.monticore.utils.Names;

import java.util.Set;

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

  @Override
  public Set<String> calculateModelNamesForType(String name) {
    Set<String> result = super.calculateModelNamesForType(name);

    while(name.contains(".")) {
      name = Names.getQualifier(name);
      result.add(name);
    }
    return result;
  }
}
