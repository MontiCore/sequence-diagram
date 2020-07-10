/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class CompleteVisibleModifierContradictionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new CompleteVisibleModifierContradiction());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xS0015");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("complete_visible_modifier_contradiction.sd", 0, 1);
  }

}
