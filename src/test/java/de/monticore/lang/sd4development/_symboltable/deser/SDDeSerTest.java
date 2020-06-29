package de.monticore.lang.sd4development._symboltable.deser;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._symboltable.SequenceDiagramSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SDDeSerTest {

  private static final String MODEL_PATH = "src/test/resources/";

  private static final String PACKAGE_PATH = MODEL_PATH + "examples/symboltable/deser/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  private SD4DevelopmentGlobalScope globalScope;

  private SD4DevelopmentScopeDeSer deSer;

  @BeforeEach
  void setup() {
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder()
            .setModelPath(new ModelPath(Paths.get(MODEL_PATH)))
            .setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION)
            .build();
    this.deSer = new SD4DevelopmentScopeDeSer();
    this.deSer.setSymbolFileExtension("sdsym");
  }

  @ParameterizedTest
  @CsvSource(
          "example_1.sd"
  )
  void testSerialization(String model) {
    // given
    ASTSDArtifact ast = loadModel(PACKAGE_PATH + model);
    assertNotNull(ast);

    // when
    String serializedSD = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());

    // then
    System.out.println(serializedSD);
    assertTrue(serializedSD.length() > 0);
  }

  @Disabled("This test currently fails, because the DeSer of SD4Development does not recognize that SDArtifact has seqeuenceDiagramSymbols")
  @Test
  void testDeserialization() {
    // given
    String serializedSD = loadSerializedModel(Paths.get(PACKAGE_PATH, "example_1.sdsym"));
    assertNotNull(serializedSD);

    // when
    ISD4DevelopmentScope deserializedSD = deSer.deserialize(serializedSD);

    // then
    assertNotNull(deserializedSD);
    assertEquals(1, deserializedSD.getLocalSequenceDiagramSymbols().size());
    SequenceDiagramSymbol sdSymbol = deserializedSD.getLocalSequenceDiagramSymbols().get(0);
    assertEquals("example_1", sdSymbol.getName());
    assertEquals("examples.symboltable.deser", sdSymbol.getPackageName());
    assertEquals(1, deserializedSD.getSubScopes().size());
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

  private String loadSerializedModel(Path serializedPath) {
    try (Stream<String> lines = Files.lines(serializedPath)) {
      return lines.collect(Collectors.joining(System.lineSeparator()));
    } catch (IOException e) {
      System.err.println("Loading serialized model: " + serializedPath + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }

  private void createSymbolTableFromAST(ASTSDArtifact ast) {
    new SD4DevelopmentSymbolTableCreatorDelegator(this.globalScope).createFromAST(ast);
  }
}
