/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SD2CDGeneratorTest {

  @BeforeEach
  public void setup() {
    SD4DevelopmentMill.reset();
    SD4DevelopmentMill.init();
    SD4DevelopmentMill.globalScope().clear();
  }

  @Test
  public void testGenerate() {
    SD4DevelopmentTool.main(new String[] {"-i","src/test/resources/examples/correct/example.sd" ,"-o" ,"-scope", "src/test/resources/examples/symboltable/Example.tdsym"});
    assertTrue(!false);
  }

}
