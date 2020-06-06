/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.SDCocoTest;
import de.se_rwth.commons.logging.Log;

//public class CommonFileExtensionCocoTest extends SDCocoTest {
//
//  @Override
//  protected void initCoCoChecker() {
//    checker.addCoCo(new CommonFileExtensionCoco());
//  }
//
//  @Override
//  public void testCocoViolation() {
//    ASTSDArtifact sd = loadModel(INCORRECT_PATH, "uncommon_file_extension.sy");
//    checker.checkAll(sd);
//    assertEquals(0, Log.getErrorCount());
//    assertEquals(1,
//        Log.getFindings().stream().filter(f -> f.buildMsg().contains("CommonFileExtensionCoco")).count());
//  }
//
//  @Override
//  public void testCorrectExamples() {
//    testAllCorrectExamples();
//    assertEquals(0, Log.getErrorCount());
//    assertEquals(0,
//        Log.getFindings().stream().filter(f -> f.buildMsg().contains("CommonFileExtensionCoco")).count());
//  }
//
//}
