/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.se_rwth.commons.Names;

import java.util.Set;

public class SD4DevelopmentGlobalScope extends SD4DevelopmentGlobalScopeTOP {

  private SD4DevelopmentGlobalScope realThis;

  public SD4DevelopmentGlobalScope() {
    realThis = this;
  }

  @Override
  public SD4DevelopmentGlobalScope getRealThis() {
    return realThis;
  }

  public void setRealThis(SD4DevelopmentGlobalScope realThis) {
    this.realThis = realThis;
  }

  @Override
  public Set<String> calculateModelNamesForOOType(String name) {
    Set<String> result = super.calculateModelNamesForOOType(name);

    while (name.contains(".")) {
      name = Names.getQualifier(name);
      result.add(name);
    }
    return result;
  }
}
