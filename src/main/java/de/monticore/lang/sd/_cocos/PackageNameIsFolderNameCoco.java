package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTPackageDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.se_rwth.commons.logging.Log;

public class PackageNameIsFolderNameCoco implements SDASTSDArtifactCoCo {

	@Override
	public void check(ASTSDArtifact node) {

		String path = node.getPath();
		String file = node.getFileName();

		if (node.packageDeclarationIsPresent()) {
			ASTPackageDeclaration packageDeclaration = node.getPackageDeclaration().get();
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
