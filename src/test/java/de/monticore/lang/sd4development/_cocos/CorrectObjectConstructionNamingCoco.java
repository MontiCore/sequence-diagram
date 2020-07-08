/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdbasis._cocos.TypeNamingConventionCoco;
import org.junit.jupiter.api.Test;

public class CorrectObjectConstructionNamingCoco extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new TypeNamingConventionCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return TypeNamingConventionCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_type_naming_conventions_constructor.sd", 0, 2);
  }

}
