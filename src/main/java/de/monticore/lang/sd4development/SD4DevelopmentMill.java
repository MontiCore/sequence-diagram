// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.sd4development;

import de.monticore.lang.sd4development.types3.SD4DevelopmentTypeCheck3;

public class SD4DevelopmentMill extends SD4DevelopmentMillTOP {

  /** additionally inits the TypeCheck */
  public static void init() {
    SD4DevelopmentMillTOP.init();
    SD4DevelopmentTypeCheck3.init();
  }

}
