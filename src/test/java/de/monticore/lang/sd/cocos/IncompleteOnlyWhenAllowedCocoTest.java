/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.IncompleteOnlyWhenAllowedCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class IncompleteOnlyWhenAllowedCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker = new SDCoCoChecker();
    checker.addCoCo(new IncompleteOnlyWhenAllowedCoco());
  }

  @Override
  public void testCocoViolation() {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "incomplete_not_allowed.sd");
    checker.checkAll(sd);
    assertEquals(4, Log.getErrorCount());
    assertEquals(4,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("IncompleteOnlyWhenAllowedCoco")).count());
  }

  @Override
  public void testCorrectExamples() {
    testAllCorrectExamples();
    assertTrue(0 == Log.getErrorCount());
    assertEquals(0,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("IncompleteOnlyWhenAllowedCoco")).count());
  }

}
