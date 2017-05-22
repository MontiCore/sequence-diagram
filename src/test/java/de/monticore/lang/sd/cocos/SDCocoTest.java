package de.monticore.lang.sd.cocos;

import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import de.monticore.ast.ASTNode;
import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.monticore.lang.sd._parser.SDParser;
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
		examples.add(parse(CORRECT_PATH + "example.sd"));
		examples.add(parse(CORRECT_PATH + "example_completeness_and_stereotypes.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_1.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_2_interactions.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_3_static.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_4_constructor.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_5_factory.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_6_stereotypes.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_7_ocl.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_8_ocl_let.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/example_9_non_causal.sd"));
		return examples;
	}

	protected ASTSDCompilationUnit parse(String path) {
		SDParser parser = new SDParser();
		Path model = Paths.get(path);
		ASTSDCompilationUnit ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSDCompilationUnit(model.toString());
			if (optAst.isPresent() && (optAst.get() instanceof ASTSDCompilationUnit)) {
				ast = (ASTSDCompilationUnit) optAst.get();
			}
		} catch (Exception e) {
			Log.error("Could not parse model " + path);
		}
		assertFalse(parser.hasErrors());
		return ast;
	}

}
