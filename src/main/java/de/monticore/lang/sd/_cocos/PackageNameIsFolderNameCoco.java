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

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDPackageDeclaration;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String path = node.getPath();
		String file = node.getFileName();

		if (node.packageDeclarationIsPresent()) {
			ASTSDPackageDeclaration packageDeclaration = node.getPackageDeclaration().get();
			String packageName = packageDeclaration.getQualifiedName().toString();
			// Check if package is suffix of path
			if (!isSuffix(path, packageName)) {
				Log.warn(this.getClass().getSimpleName() + ": The package name " + packageName + " of " + path + file
						+ " does not match its path on the file system");
			}
		}
	}

	public boolean isSuffix(String path, String packageName) {
		String pathWithDots = path.replaceAll("/", "\\.");
		return pathWithDots.endsWith("." + packageName);
	}

}
