/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AST2SDTrafoTest {

  private static final String BASE_PATH = Paths.get("src", "test", "resources", "sddiff").toString();

  private SD4DevelopmentParser parser;

  private AST2SDTrafo trafo;

  @BeforeEach
  void setup() {
    parser = new SD4DevelopmentParser();
    trafo = new AST2SDTrafo();
  }

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

  private ASTSDArtifact parse(String model) {
    try {
      return parser.parse(BASE_PATH + "/" + model + ".sd").orElseThrow(NoSuchElementException::new);
    } catch (IOException | NoSuchElementException e) {
      e.printStackTrace();
    }
    System.exit(1);
    throw new IllegalStateException("Something went wrong..");
  }

}
