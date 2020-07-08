package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._symboltable.SequenceDiagramSymbol;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

public class SD4DevelopmentSymbolTableCreatorTest {

  private static final String MODEL_PATH = "src/test/resources/";

  private static final String PACKAGE_PATH = MODEL_PATH + "examples/symboltable/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  private SD4DevelopmentGlobalScope globalScope;

  @BeforeEach
  void setup() {
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder()
            .setModelPath(new ModelPath(Paths.get(MODEL_PATH)))
            .setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION)
            .build();
  }

  @ParameterizedTest
  @CsvSource(
          "STCreationTest.sd"
  )
  void testSymbolTableCreation(String model) {
    // given
    ASTSDArtifact ast = loadModel(PACKAGE_PATH + model);
    assertNotNull(ast);

    // when
    createSymbolTableFromAST(ast);

    // then
    Optional<SequenceDiagramSymbol> sdOpt = globalScope.resolveSequenceDiagram("examples.symboltable." + FilenameUtils.removeExtension(model));
    assertTrue(sdOpt.isPresent());
    SequenceDiagramSymbol sd = sdOpt.get();
    assertEquals(3, sd.getSpannedScope().getVariableSymbols().size());
    assertEquals(1, sd.getSpannedScope().getSubScopes().size());
    assertEquals(2, sd.getSpannedScope().getSubScopes().get(0).getVariableSymbols().size());
  }

  private ASTSDArtifact loadModel(String modelPath) {
    try {
      return parser.parse(modelPath).orElseThrow(NoSuchElementException::new);
    } catch (IOException | NoSuchElementException e) {
      System.err.println("Loading model: " + modelPath + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }

  private void createSymbolTableFromAST(ASTSDArtifact ast) {
    new SD4DevelopmentSymbolTableCreatorDelegator(this.globalScope).createFromAST(ast);
  }
}
