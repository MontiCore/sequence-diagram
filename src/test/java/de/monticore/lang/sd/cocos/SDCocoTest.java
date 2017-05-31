package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertFalse;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import de.monticore.ast.ASTNode;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.monticore.lang.sd._parser.SDParser;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.monticore.lang.sd._symboltable.SDSymbolTableCreator;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;

public abstract class SDCocoTest {

	private final static String CORRECT_PATH = "src/test/resources/examples/correct/";
	protected final static String INCORRECT_PATH = "src/test/resources/examples/incorrect/";

	protected SDCoCoChecker checker;

	public SDCocoTest() {
		Log.enableFailQuick(false);
		initCoCoChecker();
	}

	@After
	public void clearLog() {
		Log.getFindings().clear();
	}

	protected abstract void initCoCoChecker();

	@Test
	public abstract void testCocoViolation();

	@Test
	public abstract void testCorrectExamples();

	protected void testAllCorrectExamples() {
		for (ASTSDCompilationUnit sd : getAllCorrectExamples()) {
			initCoCoChecker(); // Reset after each model check
			checker.checkAll(sd);
		}
	}

	private List<ASTSDCompilationUnit> getAllCorrectExamples() {
		List<ASTSDCompilationUnit> examples = new ArrayList<ASTSDCompilationUnit>();
		examples.add(loadModel(CORRECT_PATH, "example.sd"));
		examples.add(loadModel(CORRECT_PATH, "example_completeness_and_stereotypes.sd"));
		examples.add(loadModel(CORRECT_PATH, "allGrammarElements.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_1.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_2_interactions.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_3_static.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_4_constructor.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_5_factory.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_6_stereotypes.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_7_ocl.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_8_ocl_let.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_9_non_causal.sd"));
		return examples;
	}

	protected ASTSDCompilationUnit loadModel(String path, String model) {

		SDLanguage lang = new SDLanguage();
		ResolvingConfiguration config = new ResolvingConfiguration();
		config.addDefaultFilters(lang.getResolvers());

		// Parse model
		SDParser parser = lang.getParser();
		ASTSDCompilationUnit ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSDCompilationUnit(path + "/" + model);
			if (optAst.isPresent() && (optAst.get() instanceof ASTSDCompilationUnit)) {
				ast = (ASTSDCompilationUnit) optAst.get();
			}
		} catch (Exception e) {
			Log.error("Could not parse model " + path + "/" + model);
		}
		assertFalse(parser.hasErrors());

		// Build ST
		GlobalScope scope = new GlobalScope(new ModelPath(Paths.get(path)), lang, config);
		Optional<SDSymbolTableCreator> st = lang.getSymbolTableCreator(config, scope);
		if (st.isPresent()) {
			st.get().createFromAST(ast);
		}

		return ast;
	}

}
