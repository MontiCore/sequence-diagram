package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSequenceDiagram;
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
		ASTSequenceDiagram sd = parse(INCORRECT_PATH + "return_before_method.sd");
		checker.checkAll(sd);
		assertTrue(1 == Log.getErrorCount());
		assertEquals(1, Log.getFindings().stream().filter(f -> f.buildMsg().contains("CoCo1")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(0 == Log.getErrorCount());
	}

}
