/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.ObjectIdentifierUniqueCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class ObjectIdentifierUniqueCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker = new SDCoCoChecker();
    checker.addCoCo(new ObjectIdentifierUniqueCoco());
  }

  @Override
  public void testCocoViolation() {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "no_unique_names.sd");
    checker.checkAll(sd);
    assertEquals(4, Log.getErrorCount());
    assertEquals(4,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("ObjectIdentifierUniqueCoco")).count());
  }

  @Override
  public void testCorrectExamples() {
    testAllCorrectExamples();
    assertEquals(0, Log.getErrorCount());
    assertEquals(0,
        Log.getFindings().stream().filter(f -> f.buildMsg().contains("ObjectIdentifierUniqueCoco")).count());
  }

}
