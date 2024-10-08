/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang;

import de.monticore.io.paths.MCPath;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._cocos.SD4DevelopmentCoCoChecker;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScopesGenitorDelegator;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public abstract class SDCocoTest extends SDAbstractTest {

  protected final static String CORRECT_PATH = SYMBOL_PATH + "/examples/correct/";

  protected final static String INCORRECT_PATH = SYMBOL_PATH + "/examples/incorrect/";

  protected final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  protected SD4DevelopmentCoCoChecker checker;

  ByteArrayOutputStream out;

  ByteArrayOutputStream err;

  public SDCocoTest() {
    Log.enableFailQuick(false);
  }

  @BeforeEach
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    this.setupGlobalScope();
    this.checker = new SD4DevelopmentCoCoChecker();
    initCoCoChecker();

    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(out));
//    System.setErr(new PrintStream(err));
  }

  private void setupGlobalScope() {
    SD4DevelopmentMill.globalScope().setSymbolPath(new MCPath(Paths.get(SYMBOL_PATH)));
    TestUtils.setupGlobalScope(SD4DevelopmentMill.globalScope());
  }

  protected abstract void initCoCoChecker();

  protected abstract List<String> getErrorCodeOfCocoUnderTest();

  protected void testCocoViolation(String modelName, int errorCount, int logFindingsCount) {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH + "/" + modelName);
    checker.checkAll(sd);
    assertEquals(errorCount, Log.getErrorCount());
    assertEquals(logFindingsCount,
      Log.getFindings()
         .stream()
         .map(Finding::buildMsg)
         .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
         .count());
  }

  @ParameterizedTest
  @CsvSource({
    "lecture/example_1.sd",
    "lecture/example_2_interactions.sd",
    "lecture/example_3_static.sd",
    "lecture/example_4_constructor.sd",
    "lecture/example_5_factory.sd",
    "lecture/example_6_stereotypes.sd",
    "lecture/example_7_noocl.sd",
    "lecture/example_7_ocl.sd",
    "lecture/example_8_ocl_let.sd",
    "lecture/example_9_non_causal.sd",
    "example.sd",
    "example_completeness_and_stereotypes.sd",
    "allGrammarElements.sd",
    "activities.sd",
    "bid.sd",
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void testCorrectExamples(String model) {
    ASTSDArtifact sd = loadModel(CORRECT_PATH + model);
    checker.checkAll(sd);
    String msgs = Log.getFindings().stream().map(Finding::getMsg).collect(Collectors.joining(System.lineSeparator()));
    assertEquals(msgs, 0, Log.getErrorCount());
    assertEquals(msgs, 0,
      Log.getFindings()
         .stream()
         .map(Finding::buildMsg)
         .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
         .count());
  }

  public ASTSDArtifact loadModel(String modelPath) {
    try {
      ASTSDArtifact ast = parser.parse(modelPath).orElseThrow(NoSuchElementException::new);
      createSymbolTableFromAST(ast);
      return ast;
    } catch (IOException | NoSuchElementException e) {
      System.err.println("Loading model: " + modelPath + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }

  private void createSymbolTableFromAST(ASTSDArtifact ast) {
    SD4DevelopmentScopesGenitorDelegator genitor = SD4DevelopmentMill.scopesGenitorDelegator();
    genitor.createFromAST(ast);
  }
}
