package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sdbasis._symboltable.SequenceDiagramSymbolDeSer;
import de.monticore.symboltable.serialization.json.JsonObject;

public class SD4DevelopmentScopeDeSer extends SD4DevelopmentScopeDeSerTOP {

  private final SequenceDiagramSymbolDeSer sdDeSer = new SequenceDiagramSymbolDeSer();

  @Override
  protected void addSymbols(JsonObject json, ISD4DevelopmentScope scope) {
    super.addSymbols(json, scope);
    if (json.hasMember("sequenceDiagramSymbols")) {
      json.getMember("sequenceDiagramSymbols").getAsJsonArray().getValues().stream()
              .map(e -> sdDeSer.deserialize(e.toString(), scope))
              .forEach(scope::add);
    }
  }
}
