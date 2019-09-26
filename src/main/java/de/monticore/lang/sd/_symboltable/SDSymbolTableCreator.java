/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._symboltable;

import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;

import java.util.Deque;

public class SDSymbolTableCreator extends SDSymbolTableCreatorTOP {

	public SDSymbolTableCreator(ResolvingConfiguration resolvingConfig, Deque<MutableScope> scopeStack) {
		super(resolvingConfig, scopeStack);
	}

	public SDSymbolTableCreator(ResolvingConfiguration resolvingConfig, MutableScope enclosingScope) {
		super(resolvingConfig, enclosingScope);
	}

	@Override
	protected ObjectDeclarationSymbol create_ObjectDeclaration(de.monticore.lang.sd._ast.ASTObjectDeclaration ast) {
		String name = "";
		if (ast.getNameOpt().isPresent()) {
			name = ast.getName();
		} else if (ast.getOfTypeOpt().isPresent()) {
			name = ast.getOfType();
		}
		return new ObjectDeclarationSymbol(name);
	}

}
