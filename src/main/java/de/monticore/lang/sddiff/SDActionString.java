/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import lombok.Builder;
import lombok.Data;

/*
 * Simply represents an action by it's string representation.
 */
@Data
@Builder
public class SDActionString implements SDAction {

  private final String action;

  @Override
  public String toString() {
    return action;
  }
}
