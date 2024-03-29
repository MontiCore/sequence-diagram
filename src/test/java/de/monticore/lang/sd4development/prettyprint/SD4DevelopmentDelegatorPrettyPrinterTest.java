/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.prettyprint;

import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._prettyprint.SD4DevelopmentFullPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SD4DevelopmentDelegatorPrettyPrinterTest {

  private final static String CORRECT_PATH = "src/test/resources/examples/correct/";

  private SD4DevelopmentParser parser;

  private SD4DevelopmentFullPrettyPrinter prettyPrinter;

  @BeforeEach
  void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    this.parser = new SD4DevelopmentParser();
    this.prettyPrinter = new SD4DevelopmentFullPrettyPrinter(new IndentPrinter());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  @ParameterizedTest
  @CsvSource({
          "example_pretty.sd",
          "lecture/example_1.sd",
          "lecture/example_2_interactions.sd",
          "lecture/example_3_static.sd",
          "lecture/example_4_constructor.sd",
          "lecture/example_5_factory.sd",
          "lecture/example_6_stereotypes.sd",
          "lecture/example_7_noocl.sd",
          "lecture/example_8_ocl_let.sd",
          "lecture/example_9_non_causal.sd",
          "example.sd",
          "example_completeness_and_stereotypes.sd",
          "allGrammarElements.sd",
          "activities.sd"
  })
  void testCorrectExamples(String model) throws IOException {
    // given
    ASTSDArtifact ast = testParseModel(CORRECT_PATH, model);
    // when
    String printed = prettyPrinter.prettyprint(ast);
    System.out.println(printed);
    // then
    ASTSDArtifact newAst = parser.parse(new StringReader(printed)).orElseThrow(NoSuchElementException::new);
    assertTrue(ast.deepEquals(newAst));
  }

  @Nonnull
  private ASTSDArtifact testParseModel(String path, String model) {
    try {
      return parser.parse(path + model).orElseThrow(NoSuchElementException::new);
    } catch (IOException | NoSuchElementException e) {
      System.err.println("Loading model: " + model + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }
}
