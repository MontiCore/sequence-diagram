// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.sd4components;

import de.monticore.lang.sd4components.types3.SD4ComponentsTypeCheck3;

public class SD4ComponentsMill extends SD4ComponentsMillTOP {

  /** additionally inits the TypeCheck */
  public static void init() {
    SD4ComponentsMillTOP.init();
    SD4ComponentsTypeCheck3.init();
  }

}
