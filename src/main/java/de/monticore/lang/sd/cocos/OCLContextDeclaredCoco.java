package de.monticore.lang.sd.cocos;

import java.util.Optional;

import de.monticore.lang.sd._ast.ASTOCLBlock;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._cocos.SDASTSequenceDiagramCoCo;
import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

public class OCLContextDeclaredCoco implements SDASTSequenceDiagramCoCo {

	private Scope sdScope;

	@Override
	public void check(ASTSequenceDiagram node) {

		// Remember the sequence diagram scope
		if (node.getSpannedScope().isPresent()) {
			sdScope = node.getSpannedScope().get();
			System.out.println(sdScope);
		} else {
			Log.error("Grammar error: NT SequenceDiagram does not span a scope");
		}

		// Check OCL blocks for proper referencing
		for (ASTSDElement e : node.getSDElements()) {
			if (e.getOCLBlock().isPresent()) {
				// TODO go on here
				// check(e.getOCLBlock().get());
			}
		}
	}

	public void check(ASTOCLBlock node) {
		if (node.contextIsPresent()) {
			String context = node.getContext().get();
			System.out.println(node.getEnclosingScope().isPresent());
			// if(node.getEnclosingScope().isPresent()){
			Optional<Symbol> symbol = sdScope.resolve(context, ObjectDeclarationSymbol.KIND);
			if (symbol.isPresent()) {
				return;
			}
			// Could not resolve context name
			Log.error(this.getClass().getSimpleName() + ": Context " + context + " is not a declared object.",
					node.get_SourcePositionStart());
		}

	}

}
