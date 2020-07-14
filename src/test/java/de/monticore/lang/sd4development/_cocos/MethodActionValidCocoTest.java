package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Collections;
import java.util.List;

public class MethodActionValidCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new MethodActionValidCoco());
  }

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Collections.singletonList("0xB0012");
  }

  @Override
  @ParameterizedTest
  @CsvSource(
    "lecture/example_1.sd"
  )
  public void testCorrectExamples(String model) {
    super.testCorrectExamples(model);
  }

  @Test
  void testCocoViolation() {
    super.testCocoViolation("invalid_method_action.sd", 0, 1);
  }
}
