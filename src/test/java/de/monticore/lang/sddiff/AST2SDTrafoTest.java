/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.junit.jupiter.api.Test;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AST2SDTrafoTest extends SDDiffTestBase {

  private final AST2SDTrafo trafo = new AST2SDTrafo();

  @Test
  void test_rob1() {
    ASTSDArtifact ast = parse("rob1");
    SequenceDiagram sd = trafo.toSD(ast);
    assertEquals(sd, rob1());
  }

  @Test
  void test_rob2() {
    ASTSDArtifact ast = parse("rob2");
    SequenceDiagram sd = trafo.toSD(ast);
    assertEquals(sd, rob2());
  }

  @Test
  void test_rob3() {
    ASTSDArtifact ast = parse("rob3");
    SequenceDiagram sd = trafo.toSD(ast);
    assertEquals(sd, rob3());
  }

  @Test
  void test_rob4() {
    ASTSDArtifact ast = parse("rob4");
    SequenceDiagram sd = trafo.toSD(ast);
    assertEquals(sd, rob4());
  }

  @Test
  void test_rob5() {
    ASTSDArtifact ast = parse("rob5");
    SequenceDiagram sd = trafo.toSD(ast);
    assertEquals(sd, rob5());
  }

}
