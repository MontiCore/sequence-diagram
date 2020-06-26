package de.monticore.lang.sd4development._symboltable.deser;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.Assert.fail;

public class SDDeSerTest {

  private static final String MODEL_PATH = "src/test/resources/";

  private static final String CORRECT_PATH = MODEL_PATH + "examples/correct/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  private SD4DevelopmentGlobalScope globalScope;

  private SD4DevelopmentScopeDeSer deSer;

  @BeforeEach
  void setup() {
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder()
            .setModelPath(new ModelPath(Paths.get(MODEL_PATH)))
            .setSD4DevelopmentLanguage(new SD4DevelopmentLanguage())
            .build();
    this.deSer = new SD4DevelopmentScopeDeSer();
  }

  @ParameterizedTest
  @CsvSource(
          "lecture/example_1.sd"
  )
  void testSerialization(String model) {
    // given
    ASTSDArtifact ast = loadModel(CORRECT_PATH + model);

    // when
    String serializedSD = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());

    // then
    System.out.println(serializedSD);
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
    new SD4DevelopmentSymbolTableCreatorDelegator(this.globalScope).createFromAST(ast);
  }
}
