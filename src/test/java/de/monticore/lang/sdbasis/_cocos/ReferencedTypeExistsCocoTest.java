package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ReferencedTypeExistsCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReferencedTypeExistsCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xB0028");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("used_type_undefined.sd", 1, 1);
  }
}
