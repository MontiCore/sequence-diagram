/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class ImportStatementsValidCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new ImportStatementsValidCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return ImportStatementsValidCoco.class;
  }

  @Test
  public void testCocoViolation() {
    testCocoViolation("faulty_imports.sd", 2, 2);
  }

}
