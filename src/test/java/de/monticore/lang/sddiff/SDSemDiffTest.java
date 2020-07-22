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
    SDSemDiffWitness witness = witnessOpt.get();
    assertTrue(7 < witness.getWitness().size()); // may be less, but is non-deterministic
  }

  @Test
  void testSemDiff_rob2_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob1());
    assertTrue(witnessOpt.isPresent());
    SDSemDiffWitness witness = witnessOpt.get();
    assertTrue(4 < witness.getWitness().size()); // may be less, but is non-deterministic
  }

  @Test
  void testSemDiff_rob2_rob2() {
    assertFalse(sdSemDiff.semDiff(rob2(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob2(), rob3());
    assertTrue(witnessOpt.isPresent());
    SDSemDiffWitness witness = witnessOpt.get();
    assertTrue(5 < witness.getWitness().size()); // may be less, but is non-deterministic
  }

  @Test
  void testSemDiff_rob3_rob1() {
    Optional<SDSemDiffWitness> witnessOpt = sdSemDiff.semDiff(rob3(), rob1());
    assertTrue(witnessOpt.isPresent());
    SDSemDiffWitness witness = witnessOpt.get();
    assertTrue(4 < witness.getWitness().size()); // may be less, but is non-deterministic
  }

  @Test
  void testSemDiff_rob3_rob2() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob3() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob3()).isPresent());
  }
}
