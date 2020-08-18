package de.monticore.lang.sddiff;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder(builderClassName = "Builder")
public class SequenceDiagram {

  private final Set<SDObject> objects;

  @lombok.Builder.Default
  private final Set<SDObject> completeObjects = new HashSet<>();

  @lombok.Builder.Default
  private final Set<SDObject> visibleObjects = new HashSet<>();

  @lombok.Builder.Default
  private final Set<SDObject> initialObjects = new HashSet<>();

  private final List<SDInteraction> interactions;
}
