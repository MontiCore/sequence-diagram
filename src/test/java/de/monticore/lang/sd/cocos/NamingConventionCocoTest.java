/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.NamingConventionCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class NamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker = new SDCoCoChecker();
    checker.addCoCo(new NamingConventionCoco());
  }

  @Override
  public void testCocoViolation() {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "violated_naming_conventions.sd");
    checker.checkAll(sd);
    assertEquals(0, Log.getErrorCount());
    assertEquals(10, Log.getFindings().stream().filter(f -> f.buildMsg().contains("NamingConventionCoco")).count());
  }

  @Override
  public void testCorrectExamples() {
    testAllCorrectExamples();
    assertEquals(0, Log.getErrorCount());
    assertEquals(0, Log.getFindings().stream().filter(f -> f.buildMsg().contains("NamingConventionCoco")).count());
  }

}
