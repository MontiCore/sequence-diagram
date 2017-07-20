/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

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
