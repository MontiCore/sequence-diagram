package de.monticore.lang.sd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class LectureExamplesTest {

	private ASTSequenceDiagram loadModel(String modelName) throws IOException {
		// Load model
		Log.enableFailQuick(false);
		Path model = Paths.get(modelName);
		SDParser parser = new SDParser();
		Optional<ASTSequenceDiagram> sd = parser.parseSequenceDiagram(model.toString());
		assertFalse(parser.hasErrors());
		assertTrue(sd.isPresent());
		return sd.get();
	}

	@Test
	public void testExampleConstructor() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-constructor.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSDElements().size());
	}

	@Test
	public void testExampleFactory() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-factory.sd");

		// Traverse AST and check for correctness
		assertEquals(5, sd.getSDElements().size());
	}

	@Test
	public void testExampleInteractions() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-interactions.sd");

		// Traverse AST and check for correctness
		assertEquals(15, sd.getSDElements().size());
	}

	@Test
	public void testExampleNonCausal() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-non-causal.sd");

		// Traverse AST and check for correctness
		assertEquals(5, sd.getSDElements().size());
	}

	@Test
	public void testExampleOcl() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-ocl.sd");

		// Traverse AST and check for correctness
		assertEquals(10, sd.getSDElements().size());
	}

	@Test
	public void testExampleStatic() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-static.sd");

		// Traverse AST and check for correctness
		assertEquals(4, sd.getSDElements().size());
	}

	@Test
	public void testExampleStereotypes() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example-stereotypes.sd");

		// Traverse AST and check for correctness
		assertEquals(7, sd.getSDElements().size());
	}

	@Test
	public void testExample() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/lecture-example.sd");

		// Traverse AST and check for correctness
		assertEquals(8, sd.getSDElements().size());
	}

}
