/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._coco.UniqueObjectNamingCoco;
import de.se_rwth.commons.logging.Log;
import org.junit.Test;

public class UniqueObjectNamingCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new UniqueObjectNamingCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return UniqueObjectNamingCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("no_unique_names.sd", 4, 4);
  }
}
