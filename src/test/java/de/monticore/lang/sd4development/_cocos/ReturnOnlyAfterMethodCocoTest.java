/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ReturnOnlyAfterMethodCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReturnOnlyAfterMethodCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ReturnOnlyAfterMethodCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("return_before_method.sd", 0, 1);
  }

}
