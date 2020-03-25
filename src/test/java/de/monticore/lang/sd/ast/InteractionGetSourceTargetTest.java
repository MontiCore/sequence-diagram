/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.ast;

import de.monticore.lang.sd._ast.*;
import de.monticore.lang.sd._symboltable.SDLanguage;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InteractionGetSourceTargetTest {

  @Test
  public void test() throws IOException {
    SDLanguage lang = new SDLanguage();
    ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/ast", "SourceTargetTest.sd");
    List<ASTSDElement> elements = sd.getSequenceDiagram().getSDElementList();

    ASTMethodCall e1 = (ASTMethodCall) elements.get(0).getInteractionOpt().get();
    assertEquals("a", e1.getSource().getNameOpt().get());
    assertEquals("b", e1.getTarget().getNameOpt().get());

    ASTMethodCall e2 = (ASTMethodCall) elements.get(1).getInteractionOpt().get();
    assertEquals("b", e2.getSource().getNameOpt().get());
    assertEquals("a", e2.getTarget().getNameOpt().get());

    ASTReturn e3 = (ASTReturn) elements.get(2).getInteractionOpt().get();
    assertEquals("a", e3.getSource().getNameOpt().get());
    assertEquals("b", e3.getTarget().getNameOpt().get());

    ASTReturn e4 = (ASTReturn) elements.get(3).getInteractionOpt().get();
    assertEquals("b", e4.getSource().getNameOpt().get());
    assertEquals("a", e4.getTarget().getNameOpt().get());

    ASTException e5 = (ASTException) elements.get(4).getInteractionOpt().get();
    assertEquals("a", e5.getSource().getNameOpt().get());
    assertEquals("b", e5.getTarget().getNameOpt().get());

    ASTException e6 = (ASTException) elements.get(5).getInteractionOpt().get();
    assertEquals("b", e6.getSource().getNameOpt().get());
    assertEquals("a", e6.getTarget().getNameOpt().get());
  }

}
