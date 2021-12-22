/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development;

import de.monticore.io.paths.MCPath;
import de.monticore.lang.TestUtils;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentSymbolTableCompleter;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class SD4DevelopmentToolTest {

  protected static final String SYMBOL_PATH = "src/test/resources";
  protected final static String CORRECT_PATH = SYMBOL_PATH + "/examples/correct/";
  protected final static String SYMBOLS_OUT = "target/symbols/";
  ByteArrayOutputStream out;
  ByteArrayOutputStream err;

  @BeforeEach
  public void setup() {
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    this.setupGlobalScope();
    Log.getFindings().clear();
    Log.enableFailQuick(false);

    out = new ByteArrayOutputStream();
    err = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  private void setupGlobalScope() {
    SD4DevelopmentMill.globalScope().setSymbolPath(new MCPath(Paths.get(SYMBOL_PATH)));
    TestUtils.setupGlobalScope(SD4DevelopmentMill.globalScope());
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();
    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();
    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    cli.createSymbolTable(ast);

    cli.checkIntraModelCoCos(ast);
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();
    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    cli.createSymbolTable(ast);

    cli.checkAllExceptTypeCoCos(ast);
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();
    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    cli.createSymbolTable(ast);

    SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(ast.getMCImportStatementList(), ast.getPackageDeclaration());
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.add4BasicSymbols(stCompleter);
    t.setSD4DevelopmentHandler(stCompleter);
    stCompleter.setTraverser(t);

    SD4DevelopmentMill.globalScope().accept(t);

    cli.checkAllCoCos(ast);
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();
    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    cli.prettyPrint(ast, "");
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
    SD4DevelopmentTool cli = new SD4DevelopmentTool();

    ASTSDArtifact ast = cli.parse(CORRECT_PATH + model);
    Assertions.assertNotNull(ast);
    String symbolFileName = SYMBOLS_OUT + ast.getSequenceDiagram().getName() +".sdsym";
    cli.createSymbolTable(ast);
    SD4DevelopmentArtifactScope artifactScope = (SD4DevelopmentArtifactScope) ast.getEnclosingScope();

    cli.storeSymbols(ast, symbolFileName);
    ISD4DevelopmentArtifactScope loadedST = cli.loadSymbols(symbolFileName);

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

  /*****************************************************************
   ****************** CLI MAIN METHOD TESTS ************************
   ******************************************************************/

  @Test
  public void testSemDiffRefinement() {
    Log.clearFindings();

    SD4DevelopmentTool.main(new String[] {
      "-i",
      "src/test/resources/sddiff/rob1.sd",
      "src/test/resources/sddiff/rob2.sd",
      "-sd"
    });
    String printed = out.toString().trim();
    assertNotNull(printed);
    assertTrue(printed.contains("is a refinement of the input SD"));
    Assert.assertEquals(0, Log.getErrorCount());
  }

  @Test
  public void testSemDiffNoRefinement() {
    Log.clearFindings();

    SD4DevelopmentTool.main(new String[] {
      "-i",
      "src/test/resources/sddiff/rob2.sd",
      "src/test/resources/sddiff/rob1.sd",
      "-sd"
    });
    String printed = out.toString().trim();
    assertNotNull(printed);
    assertTrue(printed.contains("Diff witness:"));
    Assert.assertEquals(0, Log.getErrorCount());
  }

  @Test
  public void testPrettyPrint() {
    Log.clearFindings();

    SD4DevelopmentTool.main(new String[] {
      "-i",
      "src/test/resources/sddiff/rob1.sd",
      "-pp"
    });
    String printed = out.toString().trim();
    assertNotNull(printed);
    assertTrue(printed.contains("rob1"));
    Assert.assertEquals(0, Log.getErrorCount());
  }

  @Test
  public void testParse() {
    Log.clearFindings();
    Log.initWARN();

    SD4DevelopmentTool.main(new String[] {
      "-i",
      "src/test/resources/examples/ast/Bid1.sd"
    });
    String printed = out.toString().trim();
    assertNotNull(printed);
    Assert.assertEquals(0, Log.getErrorCount());
  }

  @Test
  public void testCoCosViolated() {
    Log.clearFindings();
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    BasicSymbolsMill.initializePrimitives();

    SD4DevelopmentTool.main(new String[] {
      "-i",
      "src/test/resources/examples/ast/Bid1.sd",
      "-c"
    });
    String printed = out.toString().trim();
    assertNotNull(printed);
    assertFalse(printed.contains("java.lang.NullPointerException"));
    Assert.assertEquals(8, Log.getErrorCount());
  }

}
