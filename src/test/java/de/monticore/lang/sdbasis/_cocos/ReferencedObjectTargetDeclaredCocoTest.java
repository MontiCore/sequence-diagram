/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class ReferencedObjectTargetDeclaredCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReferencedObjectTargetDeclaredCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ReferencedObjectTargetDeclaredCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("reference_undeclared_objects.sd", 5, 5);
  }

}
