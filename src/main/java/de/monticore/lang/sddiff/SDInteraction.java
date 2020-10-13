/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SDInteraction {

  private final String source;
  private final String action;
  private final String target;

  @Override
  public String toString() {
    return source + " -> " + target + " : " + action;
  }
}
