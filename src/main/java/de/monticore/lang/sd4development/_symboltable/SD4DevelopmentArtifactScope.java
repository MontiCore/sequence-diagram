/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import java.util.List;
import java.util.Optional;

public class SD4DevelopmentArtifactScope extends SD4DevelopmentArtifactScopeTOP {

  public SD4DevelopmentArtifactScope(String packageName, List<de.monticore.symboltable.ImportStatement> imports) {
    super(Optional.empty(), packageName, imports);
  }

  public SD4DevelopmentArtifactScope(Optional<de.monticore.lang.sd4development._symboltable.ISD4DevelopmentScope> enclosingScope, String packageName, List<de.monticore.symboltable.ImportStatement> imports) {
    super(enclosingScope, packageName, imports);
  }

  public SD4DevelopmentArtifactScope() {
    super("", new java.util.ArrayList<>());
  }

  @Override
  public String getName() {
    // every SD contains exactly one diagram symbol
    return getLocalDiagramSymbols().get(0).getName();
  }

  @Override
  public boolean isPresentName() {
    return true;
  }
}
