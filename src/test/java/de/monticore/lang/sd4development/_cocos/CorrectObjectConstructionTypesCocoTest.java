package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;

public class CorrectObjectConstructionTypesCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new CorrectObjectConstructionTypesCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return CorrectObjectConstructionTypesCoco.class;
  }

  @Test
  void testCocoViolation() {
    testCocoViolation("initialization_type_is_not_subtype_of_declaration_type.sd", 1, 1);
  }
}
