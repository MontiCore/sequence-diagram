/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.ast;

import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._symboltable.SDLanguage;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectReferenceGetDeclarationTest {

  @Test
  public void test() throws IOException {
    SDLanguage lang = new SDLanguage();
    ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/ast", "ObjectReferenceTest.sd");
    ASTInteraction i1 = sd.getSequenceDiagram().getSDElementList().get(0).getInteractionOpt().get();
    ASTObjectReference or1 = ((ASTMethodCall) i1).getTarget();
    ASTInteraction i2 = sd.getSequenceDiagram().getSDElementList().get(1).getInteractionOpt().get();
    ASTObjectReference or2 = ((ASTMethodCall) i2).getTarget();
    ASTInteraction i3 = sd.getSequenceDiagram().getSDElementList().get(2).getInteractionOpt().get();
    ASTObjectReference or3 = ((ASTMethodCall) i3).getTarget();
    ASTInteraction i4 = sd.getSequenceDiagram().getSDElementList().get(3).getInteractionOpt().get();
    ASTObjectReference or4 = ((ASTMethodCall) i4).getTarget();

    assertEquals("b", or1.getDeclaration().getNameOpt().get());
    assertEquals("B", or1.getDeclaration().getOfTypeOpt().get());

    assertEquals("e", or2.getDeclaration().getNameOpt().get());
    assertEquals("E", or2.getDeclaration().getOfTypeOpt().get());

    assertFalse(or3.getDeclaration().getNameOpt().isPresent());
    assertEquals("C", or3.getDeclaration().getOfTypeOpt().get());

    assertTrue(or4.getDeclaration().isClass());
    assertEquals("D", or4.getDeclaration().getNameOpt().get());

  }

}
