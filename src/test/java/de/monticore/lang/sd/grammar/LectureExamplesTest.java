/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.grammar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.se_rwth.commons.logging.Log;

public class LectureExamplesTest {

	private final String MODEL_SRC = "src/test/resources/examples/correct/lecture/";

	@BeforeClass
	public static void setUp() {
		Log.enableFailQuick(true);
	}

	@Test
	public void testExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_1.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(3, sd.getObjectDeclarations().size());
		assertEquals(5, sd.getSDElements().size());
	}

	@Test
	public void testExampleInteractions() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_2_interactions.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(2, sd.getObjectDeclarations().size());
		assertEquals(14, sd.getSDElements().size());
	}

	@Test
	public void testExampleStatic() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_3_static.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(2, sd.getObjectDeclarations().size());
		assertEquals(2, sd.getSDElements().size());
	}

	@Test
	public void testExampleConstructor() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_4_constructor.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(1, sd.getObjectDeclarations().size());
		assertEquals(1, sd.getSDElements().size());
	}

	@Test
	public void testExampleFactory() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_5_factory.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(2, sd.getObjectDeclarations().size());
		assertEquals(3, sd.getSDElements().size());
	}

	@Test
	public void testExampleStereotypes() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_6_stereotypes.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(3, sd.getObjectDeclarations().size());
		assertEquals(4, sd.getSDElements().size());
	}

	@Test
	public void testExampleOcl() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_7_ocl.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(4, sd.getObjectDeclarations().size());
		assertEquals(6, sd.getSDElements().size());
		assertTrue(sd.getSDElements().get(5).getSDOCL().isPresent());
	}

	public void testExampleOclLet() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_8_ocl_let.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(2, sd.getObjectDeclarations().size());
		assertEquals(4, sd.getSDElements().size());
	}

	@Test
	public void testExampleNonCausal() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sdComp = lang.loadModelWithoutCocos(MODEL_SRC, "example_9_non_causal.sd");
		ASTSequenceDiagram sd = sdComp.getSequenceDiagram();

		// Traverse AST and check for correctness
		assertEquals(3, sd.getObjectDeclarations().size());
		assertEquals(2, sd.getSDElements().size());
	}

}
