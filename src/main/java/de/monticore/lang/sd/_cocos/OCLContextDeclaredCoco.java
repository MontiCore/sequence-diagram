package de.monticore.lang.sd._cocos;

import java.util.Optional;

import de.monticore.lang.sd._ast.ASTOCLBlock;
import de.monticore.lang.sd._cocos.SDASTOCLBlockCoCo;
import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

public class OCLContextDeclaredCoco implements SDASTOCLBlockCoCo {

	@Override
	public void check(ASTOCLBlock node) {
		if (node.contextIsPresent()) {
			String context = node.getContext().get();
			Scope sdScope = node.getEnclosingScope().get();
			Optional<Symbol> symbol = sdScope.resolveLocally(context, ObjectDeclarationSymbol.KIND);
			if (symbol.isPresent()) {
				return;
			}
			// Could not resolve context name
			Log.error(this.getClass().getSimpleName() + ": Context " + context + " is not a declared object.",
					node.get_SourcePositionStart());
		}

	}

}
