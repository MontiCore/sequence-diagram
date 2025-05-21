/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._symboltable;

import com.google.common.base.Preconditions;
import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.compsymbols._symboltable.ComponentTypeSymbol;
import de.monticore.symboltable.modifiers.BasicAccessModifier;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.se_rwth.commons.SourcePosition;

import java.util.stream.Collectors;

public class ComponentType2TypeSymbolAdapter extends TypeSymbol {

  protected ComponentTypeSymbol adaptee;

  public ComponentType2TypeSymbolAdapter(ComponentTypeSymbol adaptee) {
    super(Preconditions.checkNotNull(adaptee).getName());
    this.adaptee = adaptee;
    this.accessModifier = BasicAccessModifier.PUBLIC;
    this.spannedScope = adaptee.getSpannedScope();
    this.superTypes = adaptee.getSuperComponentsList().stream().map(c -> {
      if (!c.isGenericComponentType() || c.asGenericComponentType().getTypeBindingsAsList().isEmpty()) {
        return SymTypeExpressionFactory.createTypeObject(new ComponentType2TypeSymbolAdapter(c.getTypeInfo()));
      } else {
        return SymTypeExpressionFactory.createGenerics(
          new ComponentType2TypeSymbolAdapter(c.getTypeInfo()), c.asGenericComponentType().getTypeBindingsAsList()
        );
      }
    }).collect(Collectors.toList());
  }

  protected ComponentTypeSymbol getAdaptee() {
    return adaptee;
  }

  @Override
  public void setName(String name) {
    Preconditions.checkNotNull(name);
    Preconditions.checkArgument(!name.isBlank());
    this.getAdaptee().setName(name);
  }

  @Override
  public String getName() {
    return this.getAdaptee().getName();
  }

  @Override
  public String getFullName() {
    return this.getAdaptee().getFullName();
  }

  @Override
  public IBasicSymbolsScope getSpannedScope() {
    return this.getAdaptee().getSpannedScope();
  }

  @Override
  public IBasicSymbolsScope getEnclosingScope() {
    return this.getAdaptee().getEnclosingScope();
  }

  @Override
  public SourcePosition getSourcePosition() {
    return this.getAdaptee().getSourcePosition();
  }
}
