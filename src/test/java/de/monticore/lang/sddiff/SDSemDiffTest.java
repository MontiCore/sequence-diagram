/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SDSemDiffTest {

  private SDSemDiff sdSemDiff;
  private static final String BASE_PATH = Paths.get("src", "test", "resources", "sddiff").toString();
  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  ASTSDArtifact parse(String model) {
    try {
      return parser.parse(BASE_PATH + "/" + model + ".sd").orElseThrow(NoSuchElementException::new);
    }
    catch (IOException | NoSuchElementException e) {
      e.printStackTrace();
    }
    System.exit(1);
    throw new IllegalStateException("Something went wrong..");
  }

  @BeforeEach
  void setup() {
    sdSemDiff = new SDSemDiff();
  }

  @Test
  void testSemDiff_rob1_rob1() {
    ASTSDArtifact ast = parse("rob1");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob2() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob2");
    assertFalse(sdSemDiff.semDiff(ast1, ast2).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob3() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob4() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob5() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob1() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob2() {
    ASTSDArtifact ast = parse("rob2");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob4() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob5() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob1() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob2() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
  }

  @Test
  void testSemDiff_rob3_rob3() {
    ASTSDArtifact ast = parse("rob3");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob4() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob5() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob1() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob2() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob3() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob4() {
    ASTSDArtifact ast = parse("rob4");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob4_rob5() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob1() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob2() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob3() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob4() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<List<SDInteraction>> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob5() {
    ASTSDArtifact ast = parse("rob5");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

}
