/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class CompleteVisibleModifierContradictionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new CompleteVisibleModifierContradiction());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return CompleteVisibleModifierContradiction.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("complete_visible_modifier_contradiction.sd", 0, 1);
  }

}
