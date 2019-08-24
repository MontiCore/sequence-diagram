/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import org.apache.commons.io.FilenameUtils;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.se_rwth.commons.logging.Log;

public class CommonFileExtensionCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String fileName = node.getFileName();
		String extension = FilenameUtils.getExtension(fileName);

		// Check if extension is ".sd"
		if (!extension.equals(SDLanguage.FILE_EXTENSION)) {
			Log.warn(this.getClass().getSimpleName() + ": Extension ." + extension + " of file " + fileName
					+ " is not the common SD language extension ." + SDLanguage.FILE_EXTENSION);
		}

	}

}
