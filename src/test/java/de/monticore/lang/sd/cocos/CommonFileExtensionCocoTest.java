package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.CommonFileExtensionCoco;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.se_rwth.commons.logging.Log;

public class CommonFileExtensionCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new CommonFileExtensionCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDArtifact sd = loadModel(INCORRECT_PATH, "uncommon_file_extension.sy");
		checker.checkAll(sd);
		assertEquals(1,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("CommonFileExtensionCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertTrue(0 == Log.getErrorCount());
	}

}
