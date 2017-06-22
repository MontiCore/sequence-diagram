package de.monticore.lang.sd._cocos;

import org.apache.commons.io.FilenameUtils;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;

public class SDNameIsArtifactNameCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String sdName = node.getSequenceDiagram().getName();
		String fileName = node.getFileName();
		String baseFileName = FilenameUtils.getBaseName(fileName);

		// Check if sdName == fileName without extension
		if (!sdName.equals(baseFileName)) {
			Log.warn(this.getClass().getSimpleName() + ": The sequence diagram name " + sdName
					+ " does not match the artifact name " + baseFileName);
		}

	}

}
