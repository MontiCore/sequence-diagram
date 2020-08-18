package de.monticore.lang.sddiff;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder")
public class SDObject {

  private final String name;

  @Override
  public String toString() {
    return "[" + name + "]";
  }
}
