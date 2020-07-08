/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class UniqueObjectNamingCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new UniqueObjectNamingCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return UniqueObjectNamingCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("no_unique_names.sd", 1, 1);
  }
}
