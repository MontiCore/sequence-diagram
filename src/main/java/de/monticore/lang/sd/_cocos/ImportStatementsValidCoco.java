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

import java.io.File;

import de.monticore.lang.sd._ast.ASTImportStatement;
import de.monticore.lang.sd._ast.ASTPackageDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;

public class ImportStatementsValidCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String path = node.getPath();
		String fileName = node.getFileName();
		String root = path;

		// Package declaration present?
		if (node.getPackageDeclaration().isPresent()) {
			ASTPackageDeclaration packageDeclaration = node.getPackageDeclaration().get();
			String packageName = packageDeclaration.getQualifiedName().toString();
			PackageNameIsFolderNameCoco coco2 = new PackageNameIsFolderNameCoco();
			// Check if package name is valid
			if (!coco2.isSuffix(path, packageName)) {
				Log.error(this.getClass().getSimpleName() + ": Cannot resolve import statements. The package name "
						+ packageName + " of " + path + "/" + fileName + " does not match its path on the file system",
						node.get_SourcePositionStart());
			}
			root = path.replaceAll(packageName.replaceAll("\\.", "/"), "");
		}

		// Check if for each import statement there exists a file
		for (ASTImportStatement imp : node.getImportStatements()) {
			File file = new File(root + imp.getPath(), imp.getFileName());
			if (!file.exists()) {
				Log.error(this.getClass().getSimpleName() + ": Cannot find file " + file.getAbsolutePath()
						+ " referenced in import statement", imp.get_SourcePositionStart());
			}
		}

	}

}
