/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ConstructorNameNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ConstructorObjectNameNamingConventionCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xS0005", "0xS0006");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_name_naming_conventions_constructor.sd", 0, 1);
  }

}
