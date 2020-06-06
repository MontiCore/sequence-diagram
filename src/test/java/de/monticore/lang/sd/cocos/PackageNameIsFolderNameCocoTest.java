/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.PackageNameIsFolderNameCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker = new SDCoCoChecker();
    checker.addCoCo(new PackageNameIsFolderNameCoco());
  }

  @Override
  public void testCocoViolation() {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "wrong_package.sd");
    checker.checkAll(sd);
    assertEquals(0, Log.getErrorCount());
    assertEquals(1,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("PackageNameIsFolderNameCoco")).count());
  }

  @Override
  public void testCorrectExamples() {
    testAllCorrectExamples();
    assertTrue(0 == Log.getErrorCount());
    assertEquals(0,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("PackageNameIsFolderNameCoco")).count());
  }

}
