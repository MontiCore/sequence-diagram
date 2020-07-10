package de.monticore.lang.sddiff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SDSemDiffTest {

  private SDSemDiff sdSemDiff;

  @BeforeEach
  void setup() {
    sdSemDiff = new SDSemDiff();
  }

  @Test
  void testSemDiff_rob1_rob2() {
    assertFalse(sdSemDiff.semDiff(rob1(), rob2()).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob1() {
    assertTrue(sdSemDiff.semDiff(rob2(), rob1()).isPresent());
  }

  @Test
  void testSemDiff_rob2_rob3() {
    assertTrue(sdSemDiff.semDiff(rob2(), rob3()).isPresent());
  }

  @Test
  void testSemDiff_rob3_rob2() {
    assertFalse(sdSemDiff.semDiff(rob3(), rob2()).isPresent());
  }
}
