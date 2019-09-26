/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.cocos;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._cocos.SDCoCoChecker;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		for (ASTSDArtifact sd : getAllCorrectExamples()) {
			initCoCoChecker(); // Reset after each model check
			checker.checkAll(sd);
		}
	}

	private List<ASTSDArtifact> getAllCorrectExamples() {
		List<ASTSDArtifact> examples = new ArrayList<ASTSDArtifact>();
		// Lecture examples
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_1.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_2_interactions.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_3_static.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_4_constructor.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_5_factory.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_6_stereotypes.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_7_noocl.sd"));
		//		examples.add(loadModel(CORRECT_PATH + "lecture", "example_7_ocl.sd")); //TODO: Use the real one once OCL is back

		// examples.add(loadModel(CORRECT_PATH + "lecture",
		// "example_8_ocl_let.sd"));
		examples.add(loadModel(CORRECT_PATH + "lecture", "example_9_non_causal.sd"));
		// Own examples
		examples.add(loadModel(CORRECT_PATH, "example.sd"));
		examples.add(loadModel(CORRECT_PATH, "example_completeness_and_stereotypes.sd"));
		examples.add(loadModel(CORRECT_PATH, "allGrammarElements.sd"));
		examples.add(loadModel(CORRECT_PATH, "activities.sd"));
		return examples;
	}

	protected ASTSDArtifact loadModel(String path, String model) {
		SDLanguage lang = new SDLanguage();
		System.err.println("Loading model: " + path + "/" + model);
		return lang.loadModelWithoutCocos(path, model);
	}

}
