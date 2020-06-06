/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang;

import de.monticore.lang.sd4java._cocos.SD4JavaCoCoChecker;
import de.monticore.lang.sd4java._parser.SD4JavaParser;
import de.monticore.lang.sdcore._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public abstract class SDCocoTest {

  private final static String CORRECT_PATH = "src/test/resources/examples/correct/";
  protected final static String INCORRECT_PATH = "src/test/resources/examples/incorrect/";

  private final SD4JavaParser parser = new SD4JavaParser();

  protected SD4JavaCoCoChecker checker;

  public SDCocoTest() {
    Log.enableFailQuick(false);
    initCoCoChecker();
  }

  @Before
  public void setup() {
    this.checker = new SD4JavaCoCoChecker();
  }

  @After
  public void clearLog() {
    Log.getFindings().clear();
  }

  protected abstract void initCoCoChecker();

  protected abstract Class<?> getCoCoUnderTest();

  protected void testCocoViolation(String modelName, int errorCount, int logFindingsCount) {
    ASTSDArtifact sd = loadModel(INCORRECT_PATH, modelName);
    checker.checkAll(sd);
    assertEquals(errorCount, Log.getErrorCount());
    assertEquals(logFindingsCount,
            Log.getFindings().stream().filter(f -> f.buildMsg().contains(getCoCoUnderTest().getSimpleName())).count());
  }

  @Test
  public void testCorrectExamples() {
    testAllCorrectExamples();
    assertEquals(0, Log.getErrorCount());
    assertEquals(0,
            Log.getFindings().stream().filter(f -> f.buildMsg().contains(getCoCoUnderTest().getSimpleName())).count());
  }

  protected void testAllCorrectExamples() {
    for (ASTSDArtifact sd : getAllCorrectExamples()) {
      initCoCoChecker(); // Reset after each model check
      checker.checkAll(sd);
    }
  }

  private List<ASTSDArtifact> getAllCorrectExamples() {
    List<ASTSDArtifact> examples = new ArrayList<ASTSDArtifact>();
    // Lecture examples
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_1.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_2_interactions.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_3_static.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_4_constructor.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_5_factory.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_6_stereotypes.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_7_noocl.sd"));
    //    examples.add(loadModel(CORRECT_PATH + "lecture", "example_7_ocl.sd")); //TODO: Use the real one once OCL is back

    // examples.add(loadModel(CORRECT_PATH + "lecture",
    // "example_8_ocl_let.sd"));
    examples.add(loadModel(CORRECT_PATH + "lecture", "example_9_non_causal.sd"));
    // Own examples
    examples.add(loadModel(CORRECT_PATH, "example.sd"));
    examples.add(loadModel(CORRECT_PATH, "example_completeness_and_stereotypes.sd"));
    examples.add(loadModel(CORRECT_PATH, "allGrammarElements.sd"));
    examples.add(loadModel(CORRECT_PATH, "activities.sd"));
    return examples;
  }

  protected ASTSDArtifact loadModel(String path, String model) {
    try {
      return parser.parse(path + "/" + model).get();
    } catch (IOException | NullPointerException e) {
      System.err.println("Loading model: " + path + "/" + model + " failed: " + e.getMessage());
      fail();
    }
    return null;
  }

}
