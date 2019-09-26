/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDCompleteness;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class CompletenessConsistentCoco implements SDASTSequenceDiagramCoCo {

	private Optional<ASTSDCompleteness> globalCompleteness;

	@Override
	public void check(ASTSequenceDiagram node) {
		globalCompleteness = node.getSDCompletenessOpt();
		for (ASTObjectDeclaration od : node.getObjectDeclarationList()) {
			check(od);
		}
	}

	private void check(ASTObjectDeclaration od) {
		if (globalCompleteness.isPresent() && globalCompleteness.get().isComplete()) {
			if (od.getSDCompletenessOpt().isPresent() && !od.getSDCompletenessOpt().get().isComplete()) {
				Log.error(
						this.getClass().getSimpleName()
								+ ": Completeness of sequence diagram is set to complete, but completeness of object declaration is set to a different one.",
						od.get_SourcePositionStart());

			}
		}
	}

}
