package de.monticore.lang.sd.cocos;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import de.monticore.ast.ASTNode;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
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
		for (ASTSequenceDiagram sd : getAllCorrectExamples()) {
			checker.checkAll(sd);
		}
	}

	private List<ASTSequenceDiagram> getAllCorrectExamples() {
		List<ASTSequenceDiagram> examples = new ArrayList<ASTSequenceDiagram>();
		examples.add(parse(CORRECT_PATH + "example.sd"));
		examples.add(parse(CORRECT_PATH + "lecture/lecture-example-constructor.sd"));
		return examples;
	}

	protected ASTSequenceDiagram parse(String path) {
		SDParser parser = new SDParser();
		Path model = Paths.get(path);
		ASTSequenceDiagram ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSequenceDiagram(model.toString());
			if (optAst.isPresent() && (optAst.get() instanceof ASTSequenceDiagram)) {
				ast = (ASTSequenceDiagram) optAst.get();
			}
		} catch (Exception e) {
			Log.error("Could not parse model " + path);
		}
		return ast;
	}

}
