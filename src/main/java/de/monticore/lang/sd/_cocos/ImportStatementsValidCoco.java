/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDImportStatement;
import de.monticore.lang.sd._ast.ASTSDPackageDeclaration;
import de.se_rwth.commons.logging.Log;

import java.io.File;

public class ImportStatementsValidCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String path = node.getPath();
		String fileName = node.getFileName();
		String root = path;

		// Package declaration present?
		if (node.isPresentPackageDeclaration()) {
			ASTSDPackageDeclaration packageDeclaration = node.getPackageDeclaration();
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
		for (ASTSDImportStatement imp : node.getImportStatementsList()) {
			File file = new File(root + imp.getPath(), imp.getFileName());
			if (!file.exists()) {
				Log.error(this.getClass().getSimpleName() + ": Cannot find file " + file.getAbsolutePath()
						+ " referenced in import statement", imp.get_SourcePositionStart());
			}
		}

	}

}
