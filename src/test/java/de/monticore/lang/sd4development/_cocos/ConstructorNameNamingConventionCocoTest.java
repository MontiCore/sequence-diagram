/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdbasis._cocos.ObjectNameNamingConventionCoco;
import org.junit.jupiter.api.Test;

public class ConstructorNameNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ConstructorObjectNameNamingConventionCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ConstructorObjectNameNamingConventionCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_name_naming_conventions_constructor.sd", 0, 1);
  }

}
