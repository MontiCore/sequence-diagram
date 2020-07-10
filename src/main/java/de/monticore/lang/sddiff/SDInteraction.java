package de.monticore.lang.sddiff;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder")
public class SDInteraction {

  private final SDObject source;

  private final SDAction action;

  private final SDObject target;

  @Override
  public String toString() {
    return source.toString() + " -> " + target.toString() + " : " + action.toString();
  }
}
