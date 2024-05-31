/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.util;

import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;

/**
 * Dispatcher for checking whether the source and target objects of two interactions
 * are equal to each other. The use of the dispatcher is necessary to avoid
 * instanceof checks.
 */
public final class SourceAndTargetEquals implements SDBasisVisitor2, SD4DevelopmentVisitor2 {

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
  public void visit(ASTSDObjectSource s) {
    this.objectSource = s;
  }

  @Override
  public void visit(ASTSDObjectTarget s) {
    this.objectTarget = s;
  }

  @Override
  public void visit(ASTSDClass c) {
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
    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.traverser();
    traverser.add4SDBasis(this);
    traverser.add4SD4Development(this);
    source.accept(traverser);
    isProcessingSource = false;
    target.accept(traverser);
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
