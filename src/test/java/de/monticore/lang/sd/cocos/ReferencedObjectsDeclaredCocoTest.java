/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

//import static org.junit.Assert.assertEquals;
//
//import de.monticore.lang.SDCocoTest;
//import de.monticore.lang.sd._ast.ASTSDArtifact;
//import de.monticore.lang.sd._cocos.ReferencedObjectsDeclaredCoco;
//import de.monticore.lang.sd._cocos.SDCoCoChecker;
//import de.se_rwth.commons.logging.Log;
//
//public class ReferencedObjectsDeclaredCocoTest extends SDCocoTest {
//
//  @Override
//  protected void initCoCoChecker() {
//    checker = new SDCoCoChecker();
//    checker.addCoCo(new ReferencedObjectsDeclaredCoco());
//  }
//
//  @Override
//  public void testCocoViolation() {
//    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "reference_undeclared_objects.sd");
//    checker.checkAll(sd);
//    assertEquals(6, Log.getErrorCount());
//    assertEquals(6,
//        Log.getFindings().stream().filter(f -> f.buildMsg().contains("ReferencedObjectsDeclaredCoco")).count());
//  }
//
//  @Override
//  public void testCorrectExamples() {
//    testAllCorrectExamples();
//    assertEquals(0, Log.getErrorCount());
//    assertEquals(0,
//        Log.getFindings().stream().filter(f -> f.buildMsg().contains("ReferencedObjectsDeclaredCoco")).count());
//  }
//
//}
