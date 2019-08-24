/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._ast;

import java.util.Optional;

import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

public class ASTObjectReference extends ASTObjectReferenceTOP {

	public ASTObjectReference() {
		super();
	}

	public ASTObjectReference(String name, ASTObjectDeclaration inlineDeclaration) {
		super(name, inlineDeclaration);
	}

	/**
	 * Gets the AST node of the object declaration in which the referenced
	 * object was initially declared.
	 * 
	 * Requires the symbol table to be built in advance.
	 * 
	 * @return
	 */
	public ASTObjectDeclaration getDeclaration() {
		// Local inline declaration: Just return it
		if (inlineDeclarationIsPresent()) {
			return getInlineDeclaration().get();
		}

		// Otherwise: Scan through AST
		Scope sdScope = getEnclosingScope().get();
		Optional<Symbol> symbol = sdScope.resolveLocally(getName().get(), ObjectDeclarationSymbol.KIND);
		if (!symbol.isPresent()) {
			Log.warn("Cannot resolve declaration to object " + getName().get());
		}
		return (ASTObjectDeclaration) symbol.get().getAstNode().get();
	}

}
