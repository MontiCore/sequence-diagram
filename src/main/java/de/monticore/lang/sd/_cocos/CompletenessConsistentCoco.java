package de.monticore.lang.sd._cocos;

import java.util.Optional;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDCompleteness;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._cocos.SDASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

public class CompletenessConsistentCoco implements SDASTSequenceDiagramCoCo {

	private Optional<ASTSDCompleteness> globalCompleteness;

	@Override
	public void check(ASTSequenceDiagram node) {
		globalCompleteness = node.getSDCompleteness();
		for (ASTObjectDeclaration od : node.getObjectDeclarations()) {
			check(od);
		}
	}

	private void check(ASTObjectDeclaration od) {
		if (globalCompleteness.isPresent() && globalCompleteness.get().isComplete()) {
			if (od.getSDCompleteness().isPresent() && !od.getSDCompleteness().get().isComplete()) {
				Log.error(
						this.getClass().getSimpleName()
								+ ": Completeness of sequence diagram is set to complete, but completeness of object declaration is set to a different one.",
						od.get_SourcePositionStart());

			}
		}
	}

}
