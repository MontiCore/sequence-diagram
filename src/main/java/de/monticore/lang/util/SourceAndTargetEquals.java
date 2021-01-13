/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.util;

import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.*;

/**
 * Dispatcher for checking whether the source and target objects of two interactions
 * are equal to each other. The use of the dispatcher is necessary to avoid
 * instanceof checks.
 */
public final class SourceAndTargetEquals implements InteractionEntityDispatcher {

  private boolean isProcessingSource = false;

  private ASTSDObjectSource objectSource;
  private ASTSDObjectTarget objectTarget;

  private ASTSDClass classSource;
  private ASTSDClass classTarget;

  public SourceAndTargetEquals() {
    reset();
  }

  private void reset() {
    objectSource = null;
    objectTarget = null;
    classSource = null;
    classTarget = null;
  }

  @Override
  public void handle(ASTSDObjectSource s) {
    this.objectSource = s;
  }

  @Override
  public void handle(ASTSDObjectTarget s) {
    this.objectTarget = s;
  }

  @Override
  public void handle(ASTSDClass c) {
    if (isProcessingSource) {
      this.classSource = c;
    }
    else {
      this.classTarget = c;
    }
  }

  public boolean equals(ASTSDSource source, ASTSDTarget target) {
    reset();
    isProcessingSource = true;
    source.accept(this);
    isProcessingSource = false;
    target.accept(this);
    return isSameClass() || isSameObject();
  }

  private boolean isSameObject() {
    // both are objects => names have to be equal
    return !(objectSource != null && objectTarget != null)
      || objectSource.getName().equals(objectTarget.getName());
  }

  private boolean isSameClass() {
    // both are class => MCObjectType's have to be equal
    return !(classSource != null && classTarget != null)
      || classSource.getMCObjectType().deepEquals(classTarget.getMCObjectType());
  }
}
