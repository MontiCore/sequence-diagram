/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.SDCocoTest;
import org.junit.Test;

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
