package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
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
		ASTSDCompilationUnit sd = loadModel(INCORRECT_PATH + "return_before_method.sd");
		checker.checkAll(sd);
		assertTrue(1 == Log.getErrorCount());
		assertEquals(1,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("ReturnOnlyAfterMethodCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(4 == Log.getErrorCount());
	}

}
