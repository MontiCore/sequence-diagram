/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class ObjectTypeNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ObjectTypeNamingConventionCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ObjectTypeNamingConventionCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_naming_conventions.sd", 0, 2);
  }

}
