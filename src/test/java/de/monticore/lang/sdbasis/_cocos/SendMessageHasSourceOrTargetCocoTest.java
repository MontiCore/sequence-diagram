/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class SendMessageHasSourceOrTargetCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new SendMessageHasSourceOrTargetCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return SendMessageHasSourceOrTargetCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("send_message_no_target_source_specified.sd", 1, 1);
  }
}
