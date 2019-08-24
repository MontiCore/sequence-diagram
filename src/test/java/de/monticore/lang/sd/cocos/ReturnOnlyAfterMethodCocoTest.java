/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.ReturnOnlyAfterMethodCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class ReturnOnlyAfterMethodCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new ReturnOnlyAfterMethodCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDArtifact sd = loadModel(INCORRECT_PATH, "return_before_method.sd");
		checker.checkAll(sd);
		assertEquals(0, Log.getErrorCount());
		assertEquals(1,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("ReturnOnlyAfterMethodCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertEquals(0, Log.getErrorCount());
		assertEquals(4,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("ReturnOnlyAfterMethodCoco")).count());
	}

}
