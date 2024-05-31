/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.types.check.CompKindExpression;
import de.monticore.types.check.CompTypeCheckResult;
import de.monticore.types.check.ISynthesizeComponent;
import de.monticore.types.check.SynthesizeCompTypeFromMCBasicTypes;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class FullSDBasisComponentsSynthesizer implements ISynthesizeComponent {

  protected SDBasisTraverser traverser;

  protected CompTypeCheckResult resultWrapper;

  public void init() {
    this.traverser = SDBasisMill.traverser();
    this.resultWrapper = new CompTypeCheckResult();
    SynthesizeCompTypeFromMCBasicTypes synFromBasic = new SynthesizeCompTypeFromMCBasicTypes(resultWrapper);
    traverser.setMCBasicTypesHandler(synFromBasic);
  }

  public SDBasisTraverser getTraverser() {
    return traverser;
  }

  public Optional<CompKindExpression> getResult() {
    return resultWrapper.getResult();
  }

  @Override
  public Optional<CompKindExpression> synthesize(@NonNull ASTMCType mcType) {
    this.init();
    mcType.accept(this.getTraverser());
    return this.getResult();
  }
}
