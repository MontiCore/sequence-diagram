/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.monticore.lang.sdcore._coco.SDNameIsArtifactNameCoco;
import de.se_rwth.commons.logging.Log;
import org.junit.Test;

public class SDNameIsArtifactNameCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new SDNameIsArtifactNameCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return SDNameIsArtifactNameCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("artifact_not_sd_name.sd", 0, 1);
  }

}
