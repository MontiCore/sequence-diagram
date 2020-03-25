/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.ast;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._symboltable.SDLanguage;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SDCompletenessEvaluationTest {

  @Test
  public void test() throws IOException {
    SDLanguage lang = new SDLanguage();
    ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct",
        "example_completeness_and_stereotypes.sd");
    ASTObjectDeclaration od;

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(0);
    assertFalse(od.getSDCompletenessOpt().isPresent());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(1);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertFalse(od.getSDCompletenessOpt().get().isComplete());
    assertTrue(od.getSDCompletenessOpt().get().isFree());
    assertFalse(od.getSDCompletenessOpt().get().isInitial());
    assertFalse(od.getSDCompletenessOpt().get().isVisible());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(2);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertFalse(od.getSDCompletenessOpt().get().isComplete());
    assertTrue(od.getSDCompletenessOpt().get().isFree());
    assertFalse(od.getSDCompletenessOpt().get().isInitial());
    assertFalse(od.getSDCompletenessOpt().get().isVisible());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(3);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertTrue(od.getSDCompletenessOpt().get().isComplete());
    assertFalse(od.getSDCompletenessOpt().get().isFree());
    assertFalse(od.getSDCompletenessOpt().get().isInitial());
    assertFalse(od.getSDCompletenessOpt().get().isVisible());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(4);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertTrue(od.getSDCompletenessOpt().get().isComplete());
    assertFalse(od.getSDCompletenessOpt().get().isFree());
    assertFalse(od.getSDCompletenessOpt().get().isInitial());
    assertFalse(od.getSDCompletenessOpt().get().isVisible());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(5);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertFalse(od.getSDCompletenessOpt().get().isComplete());
    assertFalse(od.getSDCompletenessOpt().get().isFree());
    assertFalse(od.getSDCompletenessOpt().get().isInitial());
    assertTrue(od.getSDCompletenessOpt().get().isVisible());

    od = sd.getSequenceDiagram().getObjectDeclarationList().get(6);
    assertTrue(od.getSDCompletenessOpt().isPresent());
    assertFalse(od.getSDCompletenessOpt().get().isComplete());
    assertFalse(od.getSDCompletenessOpt().get().isFree());
    assertTrue(od.getSDCompletenessOpt().get().isInitial());
    assertFalse(od.getSDCompletenessOpt().get().isVisible());

  }

}
