/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.monticore.lang.sd._cocos.StaticMethodCallOnlyReferencesClassesCoco;
import de.se_rwth.commons.logging.Log;

public class StaticMethodCallOnlyReferencesClassesCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new StaticMethodCallOnlyReferencesClassesCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDArtifact sd = loadModel(INCORRECT_PATH, "static_method_refers_to_object.sd");
		checker.checkAll(sd);
		assertEquals(5, Log.getErrorCount());
		assertEquals(5, Log.getFindings().stream()
				.filter(f -> f.buildMsg().contains("StaticMethodCallOnlyReferencesClassesCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertEquals(0, Log.getErrorCount());
		assertEquals(0, Log.getFindings().stream()
				.filter(f -> f.buildMsg().contains("StaticMethodCallOnlyReferencesClassesCoco")).count());
	}

}
