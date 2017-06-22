package de.monticore.lang.sd._ast;

import java.util.List;

public class ASTSDArtifact extends ASTSDArtifactTOP {

	protected String path;
	protected String fileName;

	public ASTSDArtifact() {
		super();
	}

	public ASTSDArtifact(ASTPackageDeclaration packageDeclaration, List<ASTImportStatement> importStatements,
			ASTSequenceDiagram sequenceDiagram) {
		super(packageDeclaration, importStatements, sequenceDiagram);
	}

	public void setFileName(String path, String name) {
		this.path = path;
		this.fileName = name;
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

}
