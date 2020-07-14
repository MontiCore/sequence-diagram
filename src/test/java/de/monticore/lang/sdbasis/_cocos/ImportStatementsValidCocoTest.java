/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class ImportStatementsValidCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ImportStatementsValidCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xB0016");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("faulty_imports.sd", 1, 1);
  }

}
