/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components;

import de.monticore.io.paths.MCPath;
import de.monticore.lang.SDAbstractTest;
import de.monticore.lang.TestUtils;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SD4ComponentsToolTest extends SDAbstractTest {

  protected final static String CORRECT_PATH = SYMBOL_PATH + "/sd4components/correct/";
  protected final static String INCORRECT_PATH = SYMBOL_PATH + "/sd4components/incorrect/";

  ByteArrayOutputStream out;
  ByteArrayOutputStream err;

  @BeforeEach
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    SD4ComponentsMill.reset();
    SD4ComponentsMill.init();
    this.setupGlobalScope();

    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  private void setupGlobalScope() {
    SD4ComponentsMill.globalScope().setSymbolPath(new MCPath(Paths.get(SYMBOL_PATH)));
    TestUtils.setupGlobalScope(SD4ComponentsMill.globalScope());
  }

  @ParameterizedTest
  @CsvSource({"Example.sd",})
  public void toolParseCorrectTest(String model) {
    SD4ComponentsTool tool = new SD4ComponentsTool();
    ASTSDArtifact ast = tool.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    checkOnlyExpectedErrorsPresent();
  }

  @ParameterizedTest
  @CsvSource({"Example.sd",})
  public void toolCreateSymbolTableCorrectTest(String model) {
    SD4ComponentsTool tool = new SD4ComponentsTool();
    ASTSDArtifact ast = tool.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    tool.createSymbolTable(ast);
    checkOnlyExpectedErrorsPresent();
  }

  @ParameterizedTest
  @CsvSource({"Example.sd",})
  public void toolAllCocosCorrectTest(String model) {
    SD4ComponentsTool tool = new SD4ComponentsTool();
    ASTSDArtifact ast = tool.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    tool.createSymbolTable(ast);

    tool.runDefaultCoCos(ast);
    checkOnlyExpectedErrorsPresent();
  }

  @ParameterizedTest
  @CsvSource({"Example.sd",})
  public void toolPrettyPrintNoErrorTest(String model) {
    SD4ComponentsTool tool = new SD4ComponentsTool();
    ASTSDArtifact ast = tool.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    tool.prettyPrint(ast, "");
    checkOnlyExpectedErrorsPresent();
  }

  @ParameterizedTest
  @MethodSource("provideIncorrectAndErrorCodes")
  public void toolAllCocosOnlyExpectedErrorTest(String model, String[] errors) {
    SD4ComponentsTool tool = new SD4ComponentsTool();

    ASTSDArtifact ast = tool.parse(INCORRECT_PATH + model);
    Assertions.assertNotNull(ast, Log.getFindings().toString());
    tool.createSymbolTable(ast);

    tool.runDefaultCoCos(ast);
    checkOnlyExpectedErrorsPresent(errors);
  }

  protected static Stream<Arguments> provideIncorrectAndErrorCodes() {
    return Stream.of(
      Arguments.of("MultipleSenders.sd", new String[]{"0xB5003", "0xB5003"}),
      Arguments.of("MissingVariable.sd", new String[]{"0xA0240", "0xB5001", "0xB5003", "0xD0104"}),
      Arguments.of("WrongConditionType.sd", new String[]{"0xB5004"}),
      Arguments.of("WrongComponentNamingConvention.sd", new String[]{"0xB0017"}),
      Arguments.of("WrongMessageTiming.sd", new String[]{"0xB5002"}),
      Arguments.of("WrongMessageType.sd", new String[]{"0xB5000", "0xB5001", "0xB5001", "0xB5001"}),
      Arguments.of("WrongUniqueComponent.sd", new String[]{"0xB5007"}),
      Arguments.of("WrongUniqueVariable.sd", new String[]{"0xB5007", "0xB5007"}),
      Arguments.of("WrongVariableDeclarationType.sd", new String[]{"0xB5005"})
    );
  }

  /*****************************************************************
   ****************** Tool MAIN METHOD TESTS ************************
   ******************************************************************/

  @Test
  public void testParse() {
    SD4ComponentsTool.main(new String[]{"-c", "-i", CORRECT_PATH, "-path", SYMBOL_PATH});
    String printed = out.toString().trim();
    Assertions.assertNotNull(printed);
    checkOnlyExpectedErrorsPresent();
  }
}
