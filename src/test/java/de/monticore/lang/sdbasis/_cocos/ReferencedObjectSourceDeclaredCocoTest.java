/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class ReferencedObjectSourceDeclaredCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ReferencedObjectSourceDeclaredCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ReferencedObjectSourceDeclaredCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("reference_undeclared_objects.sd", 4, 4);
  }

}
