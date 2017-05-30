package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class NamingConventionCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new NamingConventionCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDCompilationUnit sd = loadModel(INCORRECT_PATH, "violated_naming_conventions.sd");
		checker.checkAll(sd);
		assertTrue(8 == Log.getErrorCount());
		assertEquals(8, Log.getFindings().stream().filter(f -> f.buildMsg().contains("NamingConventionCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(0 == Log.getErrorCount());
	}

}
