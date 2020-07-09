/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TypeNamingConventionCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new TypeNamingConventionCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xS0023");
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("violated_type_naming_conventions.sd", 0, 2);
  }
}
