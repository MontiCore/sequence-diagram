/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SDParserTest extends SDAbstractTest {

  private final static String CORRECT_PATH = SYMBOL_PATH + "/examples/correct/";
  protected final static String INCORRECT_PATH = SYMBOL_PATH + "/examples/incorrect/";

  private SD4DevelopmentParser parser;

  @BeforeEach
  void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    this.parser = new SD4DevelopmentParser();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
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
    "size.sd"
  })
  void testCorrectExamples(String model) {
    testParseModel(CORRECT_PATH, model);
  }

  @ParameterizedTest
  @CsvSource({
    "artifact_not_sd_name.sd",
    "complete_visible_modifier_contradiction.sd",
    "incomplete_not_allowed.sd",
    "initialization_type_is_not_subtype_of_declaration_type.sd",
    "no_unique_names.sd",
    "ocl_context_not_declared.sd",
    "reference_undeclared_objects.sd",
    "reference_undeclared_objects_with_import.sd",
    "return_before_method.sd",
    "static_method_refers_to_object.sd",
    "uncommon_file_extension.sy",
    "violated_type_naming_conventions_constructor.sd",
    "wrong_package.sd",
    "end_call_no_target_source_specified.sd",
    "send_message_no_target_source_specified.sd",
    "violated_name_naming_conventions.sd",
    "violated_name_naming_conventions_constructor.sd",
    "violated_type_naming_conventions.sd"
  })
  void testInCorrectExamples(String model) {
    testParseModel(INCORRECT_PATH, model);
  }

  private void testParseModel(String path, String model) {
    try {
      Optional<ASTSDArtifact> ast = parser.parse(path + model);
      assertTrue(ast.isPresent(), "Failed to parse model: " + model);
    } catch (IOException | NoSuchElementException e) {
      System.err.println("Loading model: " + model + " failed: " + e.getMessage());
      fail();
    }
  }
}
