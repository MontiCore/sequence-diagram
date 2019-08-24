/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertEquals;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.monticore.lang.sd._cocos.SDNameIsArtifactNameCoco;
import de.se_rwth.commons.logging.Log;

public class SDNameIsArtifactNameCocoTest extends SDCocoTest {

	@Override
	protected void initCoCoChecker() {
		checker = new SDCoCoChecker();
		checker.addCoCo(new SDNameIsArtifactNameCoco());
	}

	@Override
	public void testCocoViolation() {
		ASTSDArtifact sd = loadModel(INCORRECT_PATH, "artifact_not_sd_name.sd");
		checker.checkAll(sd);
		assertEquals(0, Log.getErrorCount());
		assertEquals(1,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("SDNameIsArtifactNameCoco")).count());
	}

	@Override
	public void testCorrectExamples() {
		testAllCorrectExamples();
		assertEquals(0, Log.getErrorCount());
		assertEquals(0,
				Log.getFindings().stream().filter(f -> f.buildMsg().contains("SDNameIsArtifactNameCoco")).count());
	}

}
