/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class ObjectNameNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ObjectNameNamingConventionCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xS0017", "0xS0025");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_name_naming_conventions.sd", 0, 2);
  }

}
