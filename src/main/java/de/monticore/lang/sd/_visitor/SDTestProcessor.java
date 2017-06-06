package de.monticore.lang.sd._visitor;

import de.monticore.lang.sd._ast.ASTJava;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTOCLBlock;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;

public abstract class SDTestProcessor implements SDVisitor {

	protected abstract void handleMethodCall(ASTMethodCall call);

	protected abstract void handleJava(ASTJava java);

	protected abstract void handleOCL(ASTOCLBlock ocl);

	public void visitSD(ASTSequenceDiagram sd) {
		this.handle(sd);
	}

	@Override
	public void visit(ASTMethodCall call) {
		handleMethodCall(call);
	}

	@Override
	public void visit(ASTJava java) {
		handleJava(java);
	}

	@Override
	public void visit(ASTOCLBlock ocl) {
		handleOCL(ocl);
	}
}
