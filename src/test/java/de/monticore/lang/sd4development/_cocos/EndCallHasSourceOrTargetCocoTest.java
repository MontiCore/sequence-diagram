/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdbasis._cocos.SendMessageHasSourceOrTargetCoco;
import de.monticore.lang.sdbasis._cocos.UniqueObjectNamingCoco;
import org.junit.jupiter.api.Test;

public class EndCallHasSourceOrTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new EndCallHasSourceOrTargetCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return EndCallHasSourceOrTargetCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("end_call_no_target_source_specified.sd", 1, 1);
  }
}
