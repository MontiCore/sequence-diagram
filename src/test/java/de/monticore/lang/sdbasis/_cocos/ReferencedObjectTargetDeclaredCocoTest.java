/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class ReferencedObjectTargetDeclaredCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReferencedObjectTargetDeclaredCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xB0020", "0xB0027");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("reference_undeclared_objects.sd", 5, 5);
  }

}
