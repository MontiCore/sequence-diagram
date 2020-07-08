/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

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
