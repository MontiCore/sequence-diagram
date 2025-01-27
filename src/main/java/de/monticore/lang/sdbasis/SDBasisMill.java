// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.sdbasis;

import de.monticore.lang.sdbasis.types3.SDBasisTypeCheck3;

public class SDBasisMill extends SDBasisMillTOP {

  /** additionally inits the TypeCheck */
  public static void init() {
    SDBasisMillTOP.init();
    SDBasisTypeCheck3.init();
  }

}
