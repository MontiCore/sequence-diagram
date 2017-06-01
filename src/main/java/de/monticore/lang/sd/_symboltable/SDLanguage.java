package de.monticore.lang.sd._symboltable;

import de.monticore.ast.ASTNode;
import de.monticore.modelloader.ModelingLanguageModelLoader;

public class SDLanguage extends de.monticore.lang.sd._symboltable.SDLanguageTOP {

	public SDLanguage() {
		super("SequenceDiagram", "sd");
	}

	@Override
	protected ModelingLanguageModelLoader<? extends ASTNode> provideModelLoader() {
		return new SDModelLoader(this);
	}

}
