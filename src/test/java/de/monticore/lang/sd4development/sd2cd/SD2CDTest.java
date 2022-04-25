package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cd.codegen.CDGenerator;
import de.monticore.cd.codegen.CdUtilsPrinter;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateController;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.Files;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SD2CDTest {

  private final static String MODEL_PATH = "it/sd2java/src/test/resources/";
  private final static String OUTPUT_PATH = "it/sd2java/src/generated/java/";

  private static final File OUTPUT_DIR = new File(OUTPUT_PATH);

  private SD4DevelopmentParser parser;

  @BeforeAll
  public static void cleanup() {
    Files.deleteFiles(OUTPUT_DIR);
  }

  @BeforeEach
  public void setup() {
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();

    Log.getFindings().clear();
    Log.enableFailQuick(false);

    this.parser = new SD4DevelopmentParser();
  }

  @Test
  public void test() {
    ASTSDArtifact astArtifact;

    try {
      Optional<ASTSDArtifact> parseResult = this.parser.parse(MODEL_PATH + "Bid.sd");
      assertTrue(parseResult.isPresent(), "Failed to parse model: " + MODEL_PATH + "Bid.sd");

      astArtifact = parseResult.get();
    } catch (IOException |NoSuchElementException e) {
      fail("Loading model '" + MODEL_PATH + "Bid.sd" + "' failed: " + e.getMessage(), e);
      return;
    }

    SD4DevelopmentMill.reset();
    CD4CodeMill.init();

    GlobalExtensionManagement glex = new GlobalExtensionManagement();
    glex.setGlobalValue("cdPrinter", new CdUtilsPrinter());

    GeneratorSetup generatorSetup = new GeneratorSetup();
    generatorSetup.setGlex(glex);
    generatorSetup.setOutputDirectory(new File(OUTPUT_PATH));

    SD2CDTransformer transformer = new SD2CDTransformer();

    CDGenerator generator = new CDGenerator(generatorSetup);
    String configTemplate = "sd2java.SD2Java";
    TemplateController tc = generatorSetup.getNewTemplateController(configTemplate);
    TemplateHookPoint hpp = new TemplateHookPoint(configTemplate);
    List<Object> configTemplateArgs = Arrays.asList(glex, transformer, generator);

    hpp.processValue(tc, astArtifact, configTemplateArgs);

    if (!Log.getFindings().isEmpty()) {
      StringJoiner sj = new StringJoiner("\n");
      Log.getFindings().forEach(it -> sj.add(it.toString()));

      fail(sj.toString());
    }
  }

}