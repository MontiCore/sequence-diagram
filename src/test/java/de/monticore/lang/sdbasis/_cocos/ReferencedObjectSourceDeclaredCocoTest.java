/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class ReferencedObjectSourceDeclaredCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReferencedObjectSourceDeclaredCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xB0019", "0xB0026");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("reference_undeclared_objects.sd", 4, 4);
  }

  @Test
  public void testNoCocoViolation() {
    testCocoViolation("reference_undeclared_objects_with_import.sd", 0, 0);
  }

}
