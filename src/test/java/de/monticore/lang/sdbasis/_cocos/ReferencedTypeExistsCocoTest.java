/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScopesGenitor;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScopesGenitorDelegator;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentSymbolTableCompleter;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReferencedTypeExistsCocoTest extends SDCocoTest {

  @Override
  protected void initCoCoChecker() {}

  @Override
  protected List<String> getErrorCodeOfCocoUnderTest() {
    return Arrays.asList("0xB0028", "0xB0031");
  }

  @Test
  public void testCocoViolation1() {
    ASTSDArtifact sd = loadModel("src/test/resources/examples/incorrect/used_type_undefined.sd");
    SD4DevelopmentScopesGenitorDelegator genitor = SD4DevelopmentMill.scopesGenitorDelegator();
    ISD4DevelopmentArtifactScope st = genitor.createFromAST(sd);

    SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(sd.getMCImportStatementList(), sd.getPackageDeclaration());
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSD4DevelopmentHandler(stCompleter);
    t.add4BasicSymbols(stCompleter);
    stCompleter.setTraverser(t);
    st.accept(t);

    assertEquals(1, Log.getErrorCount());
    assertEquals(1,
      Log.getFindings()
        .stream()
        .map(Finding::buildMsg)
        .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
        .count());
  }

  @Test
  public void testCocoViolation2() {
    ASTSDArtifact sd = loadModel("src/test/resources/examples/incorrect/deepTypeUsageIncorrect.sd");
    SD4DevelopmentScopesGenitorDelegator genitor = SD4DevelopmentMill.scopesGenitorDelegator();
    ISD4DevelopmentArtifactScope st = genitor.createFromAST(sd);

    SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(sd.getMCImportStatementList(), sd.getPackageDeclaration());
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSD4DevelopmentHandler(stCompleter);
    t.add4BasicSymbols(stCompleter);
    stCompleter.setTraverser(t);
    st.accept(t);

    assertEquals(1, Log.getErrorCount());
    assertEquals(1,
      Log.getFindings()
        .stream()
        .map(Finding::buildMsg)
        .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
        .count());
  }

  @Test
  public void testCorrectDeepTypeUsage() {
    ASTSDArtifact sd = loadModel("src/test/resources/examples/correct/deepTypeUsage.sd");

    SD4DevelopmentScopesGenitorDelegator genitor = SD4DevelopmentMill.scopesGenitorDelegator();
    ISD4DevelopmentArtifactScope st = genitor.createFromAST(sd);

    SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(sd.getMCImportStatementList(), sd.getPackageDeclaration());
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSD4DevelopmentHandler(stCompleter);
    t.add4BasicSymbols(stCompleter);
    stCompleter.setTraverser(t);
    st.accept(t);

    assertEquals(0, Log.getErrorCount());
    assertEquals(0,
      Log.getFindings()
        .stream()
        .map(Finding::buildMsg)
        .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
        .count());
  }
}
