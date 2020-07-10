/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

public class PackageNameIsFolderNameCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new PackageNameIsFolderNameCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xS0018");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("wrong_package.sd", 1, 1);
  }

}
