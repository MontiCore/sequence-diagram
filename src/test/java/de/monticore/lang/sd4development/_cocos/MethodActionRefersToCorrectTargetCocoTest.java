/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class MethodActionRefersToCorrectTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new MethodActionRefersToCorrectTargetCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xB0011");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("static_method_refers_to_object.sd", 3, 3);
  }
}
