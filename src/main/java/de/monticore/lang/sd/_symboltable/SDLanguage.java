/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._symboltable;

import de.monticore.ast.ASTNode;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.*;
import de.monticore.lang.sd._parser.SDParser;
import de.monticore.modelloader.ModelingLanguageModelLoader;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;

import java.nio.file.Paths;
import java.util.Optional;

public class SDLanguage extends de.monticore.lang.sd._symboltable.SDLanguageTOP {

	public static final String FILE_EXTENSION = "sd";

	public SDLanguage() {
		super("SequenceDiagram", FILE_EXTENSION);
	}

	/**
	 * Loads a specfic model of the SD language by parsing it, building the
	 * symbol table and checking all context conditions in advance.
	 * 
	 * @param path
	 *            Path to the folder where the model is in
	 * @param model
	 *            File name of the file containing an SD model
	 * @return a parsed SD AST with symbol table and checked cocos
	 */
	public ASTSDArtifact loadModel(String path, String model) {
		return loadModel(path, model, true);
	}

	public ASTSDArtifact loadModelWithoutCocos(String path, String model) {
		return loadModel(path, model, false);
	}

	private ASTSDArtifact loadModel(String path, String model, boolean checkCocos) {

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		// Parse model
		ResolvingConfiguration config = new ResolvingConfiguration();
		config.addDefaultFilters(getResolvingFilters());
		SDParser parser = getParser();
		ASTSDArtifact ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSDArtifact(path + "/" + model);
			if (optAst.isPresent() && (optAst.get() instanceof ASTSDArtifact)) {
				ast = (ASTSDArtifact) optAst.get();
			}
			ast.setFileName(path, model);
		} catch (Exception e) {
			Log.error("Could not parse model " + path + "/" + model + ". " + e.getMessage());
		}

		// Build ST
		GlobalScope scope = new GlobalScope(new ModelPath(Paths.get(path)), this, config);
		Optional<SDSymbolTableCreator> st = getSymbolTableCreator(config, scope);
		if (st.isPresent()) {
			st.get().createFromAST(ast);
		}

		// Check all context conditions
		if (checkCocos) {
			checkAllCocos(ast);
		}

		// Return parsed model
		return ast;
	}

	public void checkAllCocos(ASTSDArtifact model) {
		SDCoCoChecker checker = new SDCoCoChecker();
		checker.addCoCo(new CommonFileExtensionCoco());
		checker.addCoCo(new CompletenessConsistentCoco());
		checker.addCoCo(new ImportStatementsValidCoco());
		checker.addCoCo(new IncompleteOnlyWhenAllowedCoco());
		checker.addCoCo(new InlineObjectDefinitionWithConstructorCoco());
		checker.addCoCo(new NamingConventionCoco());
		checker.addCoCo(new ObjectIdentifierUniqueCoco());
		checker.addCoCo(new OCLContextDeclaredCoco());
		checker.addCoCo(new PackageNameIsFolderNameCoco());
		checker.addCoCo(new ReferencedObjectsDeclaredCoco());
		checker.addCoCo(new ReturnOnlyAfterMethodCoco());
		checker.addCoCo(new SDNameIsArtifactNameCoco());
		checker.addCoCo(new StaticMethodCallOnlyReferencesClassesCoco());
		checker.checkAll(model);
	}

	@Override
	protected ModelingLanguageModelLoader<? extends ASTNode> provideModelLoader() {
		return new SDModelLoader(this);
	}

}
