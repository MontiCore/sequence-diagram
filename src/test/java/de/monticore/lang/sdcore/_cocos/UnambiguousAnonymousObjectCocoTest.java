/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UnambiguousAnonymousObjectCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new UnambiguousAnonymousObjectCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return UnambiguousAnonymousObjectCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("no_unique_names.sd", 1, 1);
  }
}
