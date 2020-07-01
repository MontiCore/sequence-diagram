/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._cocos.SD4DevelopmentCoCoChecker;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public abstract class SDCocoTest {

  private static final String MODEL_PATH = "src/test/resources";

  private final static String CORRECT_PATH = MODEL_PATH + "/examples/correct/";

  private final static String INCORRECT_PATH = MODEL_PATH + "/examples/incorrect/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  private SD4DevelopmentGlobalScope globalScope;

  protected SD4DevelopmentCoCoChecker checker;

  public SDCocoTest() {
    Log.enableFailQuick(false);
  }

  @BeforeEach
  public void setup() {
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder()
            .setModelPath(new ModelPath(Paths.get(MODEL_PATH)))
            .setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION)
            .build();
    this.checker = new SD4DevelopmentCoCoChecker();
    initCoCoChecker();
    Log.getFindings().clear();
  }

  protected abstract void initCoCoChecker();

  protected abstract Class<?> getCoCoUnderTest();

  protected void testCocoViolation(String modelName, int errorCount, int logFindingsCount) {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH + "/" + modelName);
    checker.checkAll(sd);
    assertEquals(errorCount, Log.getErrorCount());
    assertEquals(logFindingsCount,
            Log.getFindings().stream().filter(f -> f.buildMsg().contains(getCoCoUnderTest().getSimpleName())).count());
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
          // "lecture/example_8_ocl_let.sd",
          "lecture/example_9_non_causal.sd",
          // "example.sd,
          "example_completeness_and_stereotypes.sd",
          "allGrammarElements.sd",
          "activities.sd"
  })
  public void testCorrectExamples(String model) {
    ASTSDArtifact sd = loadModel(CORRECT_PATH + model);
    checker.checkAll(sd);
    String msgs = Log.getFindings().stream().map(Finding::getMsg).collect(Collectors.joining(System.lineSeparator()));
    assertEquals(msgs, 0, Log.getErrorCount());
    assertEquals(msgs, 0,
            Log.getFindings().stream().filter(f -> f.buildMsg().contains(getCoCoUnderTest().getSimpleName())).count());
  }

  private ASTSDArtifact loadModel(String modelPath) {
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
    new SD4DevelopmentSymbolTableCreatorDelegatorBuilder().setGlobalScope(this.globalScope).build().createFromAST(ast);
  }


}
