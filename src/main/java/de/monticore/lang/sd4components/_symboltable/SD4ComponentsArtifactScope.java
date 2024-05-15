/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._symboltable;

import de.monticore.symboltable.ImportStatement;

import java.util.List;
import java.util.Optional;

public class SD4ComponentsArtifactScope extends SD4ComponentsArtifactScopeTOP {

  public SD4ComponentsArtifactScope(String packageName, List<ImportStatement> imports) {
    super(packageName, imports);
  }

  public SD4ComponentsArtifactScope(Optional<de.monticore.lang.sd4components._symboltable.ISD4ComponentsScope> enclosingScope, String packageName, List<de.monticore.symboltable.ImportStatement> imports) {
    super(enclosingScope, packageName, imports);
  }

  public SD4ComponentsArtifactScope() {
    super();
  }

  @Override
  public String getName() {
    // every SD contains exactly one diagram symbol
    if (!getLocalDiagramSymbols().isEmpty()) {
      return getLocalDiagramSymbols().get(0).getName();
    }
    // Default behavior for imported Artifact scopes
    return super.getName();
  }

  @Override
  public boolean isPresentName() {
    return !getLocalDiagramSymbols().isEmpty() || super.isPresentName();
  }
}
