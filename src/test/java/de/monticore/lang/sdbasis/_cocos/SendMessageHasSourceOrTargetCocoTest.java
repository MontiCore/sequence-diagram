/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class SendMessageHasSourceOrTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new SendMessageHasSourceOrTargetCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xS0022");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("send_message_no_target_source_specified.sd", 1, 1);
  }
}
