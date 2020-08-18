/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class UniqueObjectNamingCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new UniqueObjectNamingCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xB0024");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("no_unique_names.sd", 1, 1);
  }
}
