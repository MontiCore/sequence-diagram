/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.types.check.AbstractSynthesize;
import de.monticore.types.check.SynthesizeSymTypeFromMCBasicTypes;

public class FullSDBasisSynthesizer extends AbstractSynthesize {

  public FullSDBasisSynthesizer(){
    this(SDBasisMill.traverser());
  }

  public FullSDBasisSynthesizer(SDBasisTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  public void init(SDBasisTraverser traverser){
    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.setMCBasicTypesHandler(synthesizeSymTypeFromMCBasicTypes);
    traverser.add4MCBasicTypes(synthesizeSymTypeFromMCBasicTypes);
  }

}
