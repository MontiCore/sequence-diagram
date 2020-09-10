package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.SDCocoTest;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentSymbolTableCreatorDelegatorBuilder;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentSymbolTableCompleter;
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
  public void testCocoViolation() {
    ASTSDArtifact sd = loadModel("src/test/resources/examples/incorrect/used_type_undefined.sd");
    ISD4DevelopmentArtifactScope st = new SD4DevelopmentSymbolTableCreatorDelegatorBuilder().setGlobalScope(super.globalScope).build().createFromAST(sd);
    st.accept(new SD4DevelopmentSymbolTableCompleter());
    assertEquals(1, Log.getErrorCount());
    assertEquals(1,
      Log.getFindings()
        .stream()
        .map(Finding::buildMsg)
        .filter(f -> getErrorCodeOfCocoUnderTest().stream().anyMatch(f::contains))
        .count());
  }
}
