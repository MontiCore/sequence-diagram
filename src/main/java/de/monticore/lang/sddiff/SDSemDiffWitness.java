/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderClassName = "Builder")
public class SDSemDiffWitness {

  private final List<SDInteraction> witness;
}
