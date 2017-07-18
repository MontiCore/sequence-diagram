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