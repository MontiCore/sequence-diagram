/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class SDSemDiffTest {

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
  void testSemDiff_rob1_rob2() {
    assertFalse(sdSemDiff.semDiff(rob1(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob1_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob1_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob1(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob2() {
    assertFalse(sdSemDiff.semDiff(rob2(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob2_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob2() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob3() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob3()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob3_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob2() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob2());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob4_rob4() {
    assertFalse(sdSemDiff.semDiff(rob4(), rob4()).isPresent());
  }

  @Test
  void testSemDiff_rob4_rob5() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob4(), rob5());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob1());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob2() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob2());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob3());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob4() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob5(), rob4());
    assertTrue(witnessOpt.isPresent());
  }

  @Test
  void testSemDiff_rob5_rob5() {
    assertFalse(sdSemDiff.semDiff(rob5(), rob5()).isPresent());
  }
}
