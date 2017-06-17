package de.monticore.lang.sd._symboltable;

import java.util.Deque;

import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;

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
		if (ast.getName().isPresent()) {
			name = ast.getName().get();
		} else if (ast.getOfType().isPresent()) {
			name = ast.getOfType().get();
		}
		return new ObjectDeclarationSymbol(name);
	}

}
