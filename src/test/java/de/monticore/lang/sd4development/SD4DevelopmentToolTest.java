package de.monticore.lang.sd4development;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.TestUtils;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SD4DevelopmentToolTest {

  protected ISD4DevelopmentGlobalScope globalScope;
  protected static final String MODEL_PATH = "src/test/resources";
  protected final static String CORRECT_PATH = MODEL_PATH + "/examples/correct/";
  protected final static String SYMBOLS_OUT = "target/symbols/";

  @BeforeEach
  public void setup() {
    this.setupGlobalScope();
    Log.getFindings().clear();
  }

  private void setupGlobalScope() {
    this.globalScope = new SD4DevelopmentGlobalScopeBuilder()
      .setModelPath(new ModelPath(Paths.get(MODEL_PATH)))
      .setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION)
      .build();
    TestUtils.setupGlobalScope(globalScope);
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
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolParseCorrectTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    assertEquals(0, Log.getErrorCount());
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
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolIntraModelCocosCorrectTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    SD4DevelopmentTool.checkIntraModelCoCos(ast.get(), this.globalScope);
    assertEquals(0, Log.getErrorCount());
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
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolAllExceptTypeCocosCorrectTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    SD4DevelopmentTool.checkAllExceptTypeCoCos(ast.get(), this.globalScope);
    assertEquals(0, Log.getErrorCount());
  }

  @ParameterizedTest
  @CsvSource({
    "lecture/example_1.sd",
    "lecture/example_2_interactions.sd",
    "lecture/example_3_static.sd",
    "lecture/example_4_constructor.sd",
    "lecture/example_5_factory.sd",
    "lecture/example_8_ocl_let.sd",
    "example.sd",
    "example_completeness_and_stereotypes.sd",
    "allGrammarElements.sd",
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolAllCocosCorrectTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    SD4DevelopmentTool.checkAllCoCos(ast.get(), this.globalScope);
    assertEquals(0, Log.getErrorCount());
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
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolPrettyPrintNoErrorTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    SD4DevelopmentTool.prettyPrint(ast.get());
    assertEquals(0, Log.getErrorCount());
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
    "size.sd",
    "deepTypeUsage.sd"
  })
  public void toolDeSerTest(String model) throws IOException {
    Optional<ASTSDArtifact> ast = SD4DevelopmentTool.parseSDArtifact(CORRECT_PATH + model);
    assertTrue(ast.isPresent());
    String symbolFileName = SYMBOLS_OUT + ast.get().getSequenceDiagram().getName() +".sdsym";
    SD4DevelopmentTool.deriveSymbolSkeleton(ast.get(), this.globalScope);
    SD4DevelopmentArtifactScope artifactScope = (SD4DevelopmentArtifactScope) ast.get().getEnclosingScope();

    SD4DevelopmentTool.storeSymbols(ast.get(), symbolFileName);
    ISD4DevelopmentArtifactScope loadedST = SD4DevelopmentTool.loadSymbols(symbolFileName);

    assertEquals(0, loadedST.getSubScopes().size());
    assertEquals(1, loadedST.getLocalDiagramSymbols().size());
    assertEquals(1, artifactScope.getLocalDiagramSymbols().size());
    assertEquals(artifactScope.getLocalDiagramSymbols().get(0).getName(), loadedST.getLocalDiagramSymbols().get(0).getName());
    for(VariableSymbol var : artifactScope.getLocalVariableSymbols()) {
      assertTrue(loadedST.getLocalVariableSymbols().stream().anyMatch(x -> x.getFullName().equals(var.getFullName())));
    }
    for(VariableSymbol var : loadedST.getLocalVariableSymbols()) {
      assertTrue(artifactScope.getLocalVariableSymbols().stream().anyMatch(x -> x.getFullName().equals(var.getFullName())));
    }
    assertEquals(0, Log.getErrorCount());
  }

}
