package de.monticore.lang.sd._ast;

import java.util.List;

import de.monticore.types.types._ast.ASTQualifiedName;

public class ASTImportStatement extends ASTImportStatementTOP {

	public ASTImportStatement() {
		super();
	}

	public ASTImportStatement(ASTQualifiedName qualifiedName) {
		super(qualifiedName);
	}

	public String getPath() {
		String fqn = this.getQualifiedName().toString();
		fqn = fqn.substring(0, fqn.length() - getFileName().length());
		return fqn.replaceAll("\\.", "/");
	}

	public String getFileName() {
		List<String> parts = this.getQualifiedName().getParts();
		String fileName = parts.get(parts.size() - 2) + "." + parts.get(parts.size() - 1);
		return fileName;
	}

}
