package de.monticore.lang;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SD4DevelopmentPrettyPrinterTest {

  private final static String CORRECT_PATH = "src/test/resources/examples/";

  private SD4DevelopmentParser parser;

  private SD4DevelopmentPrettyPrinter prettyPrinter;

  @BeforeEach
  void setup() {
    this.parser = new SD4DevelopmentParser();
    this.prettyPrinter = new SD4DevelopmentPrettyPrinter();
    Log.enableFailQuick(false);
  }

  @ParameterizedTest
  @CsvSource({
          "example_pretty.sd"
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
