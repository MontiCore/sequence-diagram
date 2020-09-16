/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class SDSemDiffTest extends SDDiffTestBase {

  private SDSemDiff sdSemDiff;

  @BeforeEach
  void setup() {
    sdSemDiff = new SDSemDiff();
  }

  @Test
  void testSemDiff_rob1_rob1() {
    assertFalse(sdSemDiff.semDiff(rob1(), rob1()).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob1_parse() {
    ASTSDArtifact ast = parse("rob1");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob2() {
    assertFalse(sdSemDiff.semDiff(rob1(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob2_parse() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob2");
    assertFalse(sdSemDiff.semDiff(ast1, ast2).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob3_parse() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob4_parse() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob5_parse() {
    ASTSDArtifact ast1 = parse("rob1");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob1_parse() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob2() {
    assertFalse(sdSemDiff.semDiff(rob2(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob2_parse() {
    ASTSDArtifact ast = parse("rob2");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3_parse() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob4_parse() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob5_parse() {
    ASTSDArtifact ast1 = parse("rob2");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob1_parse() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob2() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob2_parse() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertFalse(sdSemDiff.semDiff(rob3(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob3() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob3()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob3_parse() {
    ASTSDArtifact ast = parse("rob3");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob4_parse() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob5_parse() {
    ASTSDArtifact ast1 = parse("rob3");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob1_parse() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob2() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob2());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob2_parse() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob3_parse() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob4() {
    assertFalse(sdSemDiff.semDiff(rob4(), rob4()).isPresent());
  }

  @Test
  void testSemDiff_rob4_rob4_parse() {
    ASTSDArtifact ast = parse("rob4");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }

  @Test
  void testSemDiff_rob4_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob5_parse() {
    ASTSDArtifact ast1 = parse("rob4");
    ASTSDArtifact ast2 = parse("rob5");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob1_parse() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob1");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob2() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob2());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob2_parse() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob2");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob3_parse() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob3");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob4_parse() {
    ASTSDArtifact ast1 = parse("rob5");
    ASTSDArtifact ast2 = parse("rob4");
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(ast1, ast2);
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob5() {
    ASTSDArtifact ast = parse("rob5");
    assertFalse(sdSemDiff.semDiff(ast, ast).isPresent());
  }
}
