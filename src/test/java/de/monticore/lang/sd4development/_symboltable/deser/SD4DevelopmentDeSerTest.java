/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable.deser;

import com.google.common.collect.LinkedListMultimap;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.TestUtils;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentDelegatorVisitor;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symboltable.serialization.JsonPrinter;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import de.monticore.lang.TestUtils;

import static org.junit.Assert.*;

public class SD4DevelopmentDeSerTest {

  private static final String MODEL_PATH = "src/test/resources/";

  private static final String PACKAGE_PATH = MODEL_PATH + "examples/symboltable/deser/";

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();

  private ISD4DevelopmentGlobalScope globalScope;

  private SD4DevelopmentScopeDeSer deSer;

  @BeforeEach
  void setup() {
    Log.enableFailQuick(false);
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder().setModelPath(new ModelPath(Paths.get(MODEL_PATH))).setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION).build();
    JsonPrinter.enableIndentation();
    TestUtils.setupGlobalScope(globalScope);
    this.deSer = new SD4DevelopmentScopeDeSer();
    this.deSer.setSymbolFileExtension("sdsym");

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  @Test
  public void testSerializationDeserTest() {
    // given
    ASTSDArtifact ast = loadModel(PACKAGE_PATH + "deser_test.sd");
    assertNotNull(ast);

    // when
    String serializedSD = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());

    // then
    assertTrue(serializedSD.length() > 0);
    deSer.deserialize(serializedSD); // test if JSON is valid
  }

  @Test
  public void testSerializationDeepUsage() {
    // given
    ASTSDArtifact ast = loadModel(PACKAGE_PATH + "deepTypeUsage.sd");
    assertNotNull(ast);

    // when
    String serializedSD = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());

    // then
    assertTrue(serializedSD.length() > 0);
    deSer.deserialize(serializedSD); // test if JSON is valid
  }

  @ParameterizedTest
  @CsvSource("deser_test.sdsym")
  void testDeserialization(String serializedModel) {
    // given
    String serializedSD = loadSerializedModel(Paths.get(PACKAGE_PATH, serializedModel));
    assertNotNull(serializedSD);

    // when
    ISD4DevelopmentScope deserializedSD = deSer.deserialize(serializedSD);

    // then
    assertNotNull(deserializedSD);
    assertEquals(1, deserializedSD.getLocalDiagramSymbols().size());
    DiagramSymbol diagramSymbol = deserializedSD.getLocalDiagramSymbols().get(0);
    assertEquals("deser_test", diagramSymbol.getName());
    assertEquals(FilenameUtils.removeExtension(serializedModel), diagramSymbol.getName());
    assertEquals("examples.symboltable.deser", diagramSymbol.getPackageName());
    assertEquals(4, deserializedSD.getSymbolsSize());
    LinkedListMultimap<String, VariableSymbol> variableSymbols = deserializedSD.getVariableSymbols();
    assertEquals(3, variableSymbols.size());

    assertTrue(variableSymbols.containsKey("kupfer912"));
    List<VariableSymbol> kupferVal = variableSymbols.get("kupfer912");
    assertEquals(1, kupferVal.size());
    assertEquals("Auction", kupferVal.get(0).getType().print());

    assertTrue(variableSymbols.containsKey("bidPol"));
    List<VariableSymbol> bidPolVal = variableSymbols.get("bidPol");
    assertEquals(1, bidPolVal.size());
    assertEquals("BiddingPolicy", bidPolVal.get(0).getType().print());

    assertTrue(variableSymbols.containsKey("timePol"));
    List<VariableSymbol> timePolVal = variableSymbols.get("timePol");
    assertEquals(1, timePolVal.size());
    assertEquals("TimingPolicy", timePolVal.get(0).getType().print());

    assertEquals(0, deserializedSD.getSubScopes().size());
  }

  private ASTSDArtifact loadModel(String modelPath) {
    try {
      ASTSDArtifact ast = parser.parse(modelPath).orElseThrow(NoSuchElementException::new);
      createSymbolTableFromAST(ast);
      SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(ast.getMCImportStatementList(), ast.getPackageDeclaration());
      this.globalScope.accept(stCompleter);

      return ast;
    }
    catch (IOException | NoSuchElementException e) {
      System.err.println("Loading model: " + modelPath + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }

  private String loadSerializedModel(Path serializedPath) {
    try (Stream<String> lines = Files.lines(serializedPath)) {
      return lines.collect(Collectors.joining(System.lineSeparator()));
    }
    catch (IOException e) {
      System.err.println("Loading serialized model: " + serializedPath + " failed: " + e.getMessage());
      fail();
    }
    throw new IllegalStateException("Something went wrong..");
  }

  private void createSymbolTableFromAST(ASTSDArtifact ast) {
    new SD4DevelopmentSymbolTableCreatorDelegator(this.globalScope).createFromAST(ast);
  }
}
