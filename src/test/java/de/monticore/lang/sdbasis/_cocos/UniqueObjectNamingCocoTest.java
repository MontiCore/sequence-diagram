/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sd4java._cocos.SD4JavaCoCoChecker;
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
