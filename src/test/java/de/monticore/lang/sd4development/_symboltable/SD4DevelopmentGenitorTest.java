/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

public class SD4DevelopmentGenitorTest {

  private static final String MODEL_PATH = "src/test/resources/";

  private static final String PACKAGE_PATH = MODEL_PATH + "examples/symboltable/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  @BeforeEach
  void setup() {
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    SD4DevelopmentMill.globalScope().setModelPath(new ModelPath(Paths.get(MODEL_PATH)));

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
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
    Optional<DiagramSymbol> sdOpt = SD4DevelopmentMill.globalScope().resolveDiagram("examples.symboltable." + FilenameUtils.removeExtension(model));
    assertTrue(sdOpt.isPresent());
    assertEquals(1, SD4DevelopmentMill.globalScope().getSubScopes().size());
    ISD4DevelopmentScope artifactScope = SD4DevelopmentMill.globalScope().getSubScopes().get(0);
    assertEquals(3, artifactScope.getVariableSymbols().size());
    assertEquals(1, artifactScope.getSubScopes().size());
    assertEquals(2, artifactScope.getSubScopes().get(0).getVariableSymbols().size());
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
    SD4DevelopmentMill.scopesGenitorDelegator().createFromAST(ast);
  }
}
