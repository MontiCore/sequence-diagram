/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._symboltable;

import com.google.common.base.Preconditions;
import de.monticore.symbols.basicsymbols._symboltable.IBasicSymbolsScope;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.compsymbols._symboltable.SubcomponentSymbol;
import de.monticore.symboltable.modifiers.BasicAccessModifier;
import de.monticore.types.check.KindOfGenericComponent;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.se_rwth.commons.SourcePosition;

/**
 * Adapts {@link SubcomponentSymbol}s to {@link VariableSymbol}s, e.g., so that they can
 * easily be referred to from expressions.
 */
public class Subcomponent2VariableAdapter extends VariableSymbol {

  protected SubcomponentSymbol adaptee;

  public Subcomponent2VariableAdapter(SubcomponentSymbol adaptee) {
    super(Preconditions.checkNotNull(adaptee).getName());
    this.adaptee = adaptee;
    this.accessModifier = BasicAccessModifier.PRIVATE;
  }

  protected SubcomponentSymbol getAdaptee() {
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
  public void setType(SymTypeExpression type) {
    throw new RuntimeException();
  }

  @Override
  public SymTypeExpression getType() {
    if (!adaptee.isTypePresent())
      return SymTypeExpressionFactory.createObscureType();
    if (!(adaptee.getType() instanceof KindOfGenericComponent) || ((KindOfGenericComponent) adaptee.getType()).getTypeBindingsAsList().isEmpty()) {
      return SymTypeExpressionFactory.createTypeObject(new Component2TypeSymbolAdapter(adaptee.getType().getTypeInfo()));
    } else {
      return SymTypeExpressionFactory.createGenerics(
        new Component2TypeSymbolAdapter(adaptee.getType().getTypeInfo()), ((KindOfGenericComponent) adaptee.getType()).getTypeBindingsAsList()
      );
    }
  }

  @Override
  public boolean isIsReadOnly() {
    return true;
  }

  @Override
  public IBasicSymbolsScope getEnclosingScope() {
    return this.getAdaptee().getEnclosingScope();
  }

  @Override
  public SourcePosition getSourcePosition() {
    return this.getAdaptee().getSourcePosition();
  }

  @Override
  public Subcomponent2VariableAdapter deepClone() {
    return new Subcomponent2VariableAdapter(this.getAdaptee());
  }
}
