package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.SDCocoTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MethodActionValidCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {
    checker.addCoCo(new MethodActionValidCoco());
  }

  @Override
  protected Class<?> getCoCoUnderTest() {
    return MethodActionValidCoco.class;
  }

  @Override
  @ParameterizedTest
  @CsvSource(
    "lecture/example_1.sd"
  )
  public void testCorrectExamples(String model) {
    super.testCorrectExamples(model);
  }
}
