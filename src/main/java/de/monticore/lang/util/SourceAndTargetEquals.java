package de.monticore.lang.util;

import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.*;

import java.util.Optional;

/**
 * Dispatcher for checking whether the source and target objects of two interactions
 * are equal to each other. The use of the dispatcher is necessary to avoid
 * instanceof checks.
 */
public final class SourceAndTargetEquals implements InteractionEntityDispatcher {

  private boolean isProcessingSource = false;

  private Optional<ASTSDObjectSource> objectSource;
  private Optional<ASTSDObjectTarget> objectTarget;

  private Optional<ASTSDClass> classSource;
  private Optional<ASTSDClass> classTarget;

  public SourceAndTargetEquals() {
    reset();
  }

  private void reset() {
    objectSource = Optional.empty();
    objectTarget = Optional.empty();
    classSource = Optional.empty();
    classTarget = Optional.empty();
  }

  @Override
  public void handle(ASTSDObjectSource s) {
    this.objectSource = Optional.of(s);
  }

  @Override
  public void handle(ASTSDObjectTarget s) {
    this.objectTarget = Optional.of(s);
  }

  @Override
  public void handle(ASTSDClass c) {
    if (isProcessingSource) {
      this.classSource = Optional.of(c);
    }
    else {
      this.classTarget = Optional.of(c);
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
    return !(objectSource.isPresent() && objectTarget.isPresent())
      || objectSource.get().getName().equals(objectTarget.get().getName());
  }

  private boolean isSameClass() {
    // both are class => MCObjectType's have to be equal
    return !(classSource.isPresent() && classTarget.isPresent())
      || classSource.get().getMCObjectType().deepEquals(classTarget.get().getMCObjectType());
  }
}
