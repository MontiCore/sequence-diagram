/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class CommonFileExtensionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new CommonFileExtensionCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return CommonFileExtensionCoco.class;
  }

  @Test
  void testCocoViolation() {
    testCocoViolation("uncommon_file_extension.sy", 0, 1);
  }

}
