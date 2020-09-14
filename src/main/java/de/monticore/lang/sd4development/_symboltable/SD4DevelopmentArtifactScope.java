/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.symboltable.ImportStatement;

import java.util.List;
import java.util.Optional;

public class SD4DevelopmentArtifactScope extends SD4DevelopmentArtifactScopeTOP {

  public SD4DevelopmentArtifactScope(String packageName, List<ImportStatement> imports) {
    super(packageName, imports);
  }

  public SD4DevelopmentArtifactScope(Optional<ISD4DevelopmentScope> enclosingScope, String packageName, List<ImportStatement> imports) {
    super(enclosingScope, packageName, imports);
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
