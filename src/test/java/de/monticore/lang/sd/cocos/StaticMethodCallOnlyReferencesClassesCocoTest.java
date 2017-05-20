package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class StaticMethodCallOnlyReferencesClassesCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new StaticMethodCallOnlyReferencesClassesCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDCompilationUnit sd = parse(INCORRECT_PATH + "static_method_refers_to_object.sd");
		checker.checkAll(sd);
		assertTrue(6 == Log.getErrorCount());
		assertEquals(6, Log.getFindings().stream()
				.filter(f -> f.buildMsg().contains("StaticMethodCallOnlyReferencesClassesCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(0 == Log.getErrorCount());
	}

}
