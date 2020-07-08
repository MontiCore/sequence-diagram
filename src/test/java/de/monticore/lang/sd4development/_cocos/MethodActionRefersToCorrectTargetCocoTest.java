/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;


public class MethodActionRefersToCorrectTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new MethodActionRefersToCorrectTargetCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return MethodActionRefersToCorrectTargetCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("static_method_refers_to_object.sd", 3, 3);
  }
}
