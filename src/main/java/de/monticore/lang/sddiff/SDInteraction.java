/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import java.util.Objects;

public class SDInteraction {

  private final String source;
  private final String action;
  private final String target;

  public SDInteraction(String source, String action, String target) {
    this.source = source;
    this.action = action;
    this.target = target;
  }

  public String getSource() {
    return source;
  }

  public String getAction() {
    return action;
  }

  public String getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return source + " -> " + target + " : " + action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SDInteraction that = (SDInteraction) o;
    return Objects.equals(source, that.source) && Objects.equals(action, that.action) && Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, action, target);
  }
}
