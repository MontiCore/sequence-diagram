package de.monticore.lang.sd._visitor;

import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDJava;
import de.monticore.lang.sd._ast.ASTSDOCL;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;

public abstract class SDTestVisitor implements SDVisitor {

	protected abstract void handleSequenceDiagram(ASTSequenceDiagram sd);

	protected abstract void handleObjectDeclaration(ASTObjectDeclaration od);

	protected abstract void handleMethodCall(ASTMethodCall call);

	protected abstract void handleReturn(ASTReturn ret);

	protected abstract void handleException(ASTException exception);

	protected abstract void handleJava(ASTSDJava java);

	protected abstract void handleOCL(ASTSDOCL ocl);

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
	public void visit(ASTReturn ret) {
		handleReturn(ret);
	}

	@Override
	public void visit(ASTException exception) {
		handleException(exception);
	}

	@Override
	public void visit(ASTSDJava java) {
		handleJava(java);
	}

	@Override
	public void visit(ASTSDOCL ocl) {
		handleOCL(ocl);
	}

	@Override
	public void endVisit(ASTSequenceDiagram sd) {
		this.executeAfterTraversal();
	}
}
