package de.monticore.lang.sd4development._symboltable.deser;

import com.google.common.collect.LinkedListMultimap;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._symboltable.SequenceDiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    this.deSer = new SD4DevelopmentScopeDeSer();
    this.deSer.setSymbolFileExtension("sdsym");
  }

  @ParameterizedTest
  @CsvSource("deser_test.sd")
  void testSerialization(String model) {
    // given
    ASTSDArtifact ast = loadModel(PACKAGE_PATH + model);
    assertNotNull(ast);

    // when
    String serializedSD = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());

    // then
    System.out.println(serializedSD);
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
    assertEquals(1, deserializedSD.getLocalSequenceDiagramSymbols().size());
    SequenceDiagramSymbol sdSymbol = deserializedSD.getLocalSequenceDiagramSymbols().get(0);
    assertEquals("deser_test", sdSymbol.getName());
    assertEquals(FilenameUtils.removeExtension(serializedModel), sdSymbol.getName());
    assertEquals("examples.symboltable.deser", sdSymbol.getPackageName());
    assertEquals(1, deserializedSD.getSubScopes().size());
    assertEquals(3, sdSymbol.getSpannedScope().getSymbolsSize());
    LinkedListMultimap<String, VariableSymbol> variableSymbols = sdSymbol.getSpannedScope().getVariableSymbols();
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

    assertEquals(0, sdSymbol.getSpannedScope().getSubScopes().size());
  }

  private ASTSDArtifact loadModel(String modelPath) {
    try {
      ASTSDArtifact ast = parser.parse(modelPath).orElseThrow(NoSuchElementException::new);
      createSymbolTableFromAST(ast);
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
