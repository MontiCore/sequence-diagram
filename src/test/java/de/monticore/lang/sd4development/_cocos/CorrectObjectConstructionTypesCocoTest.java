package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class CorrectObjectConstructionTypesCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new CorrectObjectConstructionTypesCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xS0007", "0xS0008", "0xS0009");
  }

  @Test
  void testCocoViolation() {
    testCocoViolation("initialization_type_is_not_subtype_of_declaration_type.sd", 1, 1);
  }
}
