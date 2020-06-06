/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._coco.PackageNameIsFolderNameCoco;
import de.se_rwth.commons.logging.Log;
import org.junit.Test;

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
    testCocoViolation("wrong_package.sd", 0, 1);
  }

}
