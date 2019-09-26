/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTSDOCL;
import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class OCLContextDeclaredCoco implements SDASTSDOCLCoCo {

	@Override
	public void check(ASTSDOCL node) {
		if (node.isPresentContext()) {
			String context = node.getContext();
			Scope sdScope = node.getEnclosingScope();
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
