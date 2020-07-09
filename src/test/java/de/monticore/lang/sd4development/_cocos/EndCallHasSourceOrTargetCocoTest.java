/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdbasis._cocos.SendMessageHasSourceOrTargetCoco;
import de.monticore.lang.sdbasis._cocos.UniqueObjectNamingCoco;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EndCallHasSourceOrTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new EndCallHasSourceOrTargetCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xS0010");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("end_call_no_target_source_specified.sd", 1, 1);
  }
}
