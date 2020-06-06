/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class PackageNameIsFolderNameCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new PackageNameIsFolderNameCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return PackageNameIsFolderNameCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("wrong_package.sd", 1, 1);
  }

}
