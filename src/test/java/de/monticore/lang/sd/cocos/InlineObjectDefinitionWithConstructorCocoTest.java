package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class InlineObjectDefinitionWithConstructorCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new InlineObjectDefinitionWithConstructorCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDCompilationUnit sd = loadModel(INCORRECT_PATH, "inline_objectdefinition_without_constructor.sd");
		checker.checkAll(sd);
		assertTrue(2 == Log.getErrorCount());
		assertEquals(2, Log.getFindings().stream()
				.filter(f -> f.buildMsg().contains("InlineObjectDefinitionWithConstructorCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(0 == Log.getErrorCount());
	}

}
