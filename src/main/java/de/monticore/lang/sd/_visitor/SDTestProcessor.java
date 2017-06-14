package de.monticore.lang.sd._visitor;

import de.monticore.lang.sd._ast.ASTJava;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTOCLBlock;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;

public abstract class SDTestProcessor implements SDVisitor {

	protected abstract void handleSequenceDiagram(ASTSequenceDiagram sd);

	protected abstract void handleObjectDeclaration(ASTObjectDeclaration od);

	protected abstract void handleMethodCall(ASTMethodCall call);

	protected abstract void handleJava(ASTJava java);

	protected abstract void handleOCL(ASTOCLBlock ocl);

	protected abstract void executeAfterTraversal();

	protected void visitSD(ASTSequenceDiagram sd) {
		this.handleSequenceDiagram(sd);
		this.handle(sd);
	}

	@Override
	public void visit(ASTObjectDeclaration od) {
		handleObjectDeclaration(od);
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

	@Override
	public void endVisit(ASTSequenceDiagram sd) {
		this.executeAfterTraversal();
	}
}
