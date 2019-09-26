/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._ast;

import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ASTObjectReference extends ASTObjectReferenceTOP {

	public ASTObjectReference() {
		super();
	}

    public ASTObjectReference(String name, ASTObjectDeclaration inlineDeclaration) {
        super(Optional.of(name), Optional.ofNullable(inlineDeclaration));
    }

    public ASTObjectReference(Optional<String> name, Optional<ASTObjectDeclaration> inlineDeclaration) {
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
		if (getInlineDeclarationOpt().isPresent()) {
			return getInlineDeclarationOpt().get();
		}

		// Otherwise: Scan through AST
		Scope sdScope = getEnclosingScopeOpt().get();
		Optional<Symbol> symbol = sdScope.resolveLocally(getName(), ObjectDeclarationSymbol.KIND);
		if (!symbol.isPresent()) {
			Log.warn("Cannot resolve declaration to object " + getName());
		}
		return (ASTObjectDeclaration) symbol.get().getAstNode().get();
	}

}
