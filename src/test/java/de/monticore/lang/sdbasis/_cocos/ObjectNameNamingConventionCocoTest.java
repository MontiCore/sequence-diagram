/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class ObjectNameNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ObjectNameNamingConventionCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ObjectNameNamingConventionCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_naming_conventions.sd", 0, 3);
  }

}
