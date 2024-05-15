/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._symboltable;

import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.compsymbols._symboltable.PortSymbol;
import de.monticore.symbols.compsymbols._symboltable.SubcomponentSymbol;
import de.monticore.symboltable.modifiers.AccessModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface ISD4ComponentsScope extends ISD4ComponentsScopeTOP {

  @Override
  default List<VariableSymbol> resolveAdaptedVariableLocallyMany(boolean foundSymbols,
                                                                 String name,
                                                                 AccessModifier modifier,
                                                                 Predicate<VariableSymbol> predicate) {

    List<PortSymbol> ports = resolvePortLocallyMany(foundSymbols, name, AccessModifier.ALL_INCLUSION, x -> true);
    List<SubcomponentSymbol> instances = resolveSubcomponentLocallyMany(foundSymbols, name, AccessModifier.ALL_INCLUSION, x -> true);

    List<VariableSymbol> adapters = new ArrayList<>(ports.size() + instances.size());
    for (PortSymbol port : ports) {

      if (getLocalVariableSymbols().stream().filter(v -> v instanceof Port2VariableAdapter)
        .noneMatch(v -> ((Port2VariableAdapter) v).getAdaptee().equals(port))) {

        // instantiate the adapter
        VariableSymbol adapter = new Port2VariableAdapter(port);

        // filter by modifier and predicate
        if (modifier.includes(adapter.getAccessModifier()) && predicate.test(adapter)) {

          // add the adapter to the result
          adapters.add(adapter);

          // add the adapter to the scope
          this.add(adapter);
        }
      }
    }
    for (SubcomponentSymbol instance : instances) {

      if (getLocalVariableSymbols().stream().filter(v -> v instanceof Subcomponent2VariableAdapter)
        .noneMatch(v -> ((Subcomponent2VariableAdapter) v).getAdaptee().equals(instance))) {

        // instantiate the adapter
        VariableSymbol adapter = new Subcomponent2VariableAdapter(instance);

        // filter by modifier and predicate
        if (modifier.includes(adapter.getAccessModifier()) && predicate.test(adapter)) {

          // add the adapter to the result
          adapters.add(adapter);

          // add the adapter to the scope
          this.add(adapter);
        }
      }
    }
    return adapters;
  }
}
