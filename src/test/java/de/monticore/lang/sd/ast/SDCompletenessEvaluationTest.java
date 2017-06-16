package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class SDCompletenessEvaluationTest {

	private ASTSDArtifact loadModel(String modelName) throws IOException {
		// Load model
		Log.enableFailQuick(false);
		Path model = Paths.get(modelName);
		SDParser parser = new SDParser();
		Optional<ASTSDArtifact> sd = parser.parseSDArtifact(model.toString());
		assertFalse(parser.hasErrors());
		assertTrue(sd.isPresent());
		return sd.get();
	}

	@Test
	public void test() throws IOException {
		ASTSDArtifact sd = loadModel("src/test/resources/examples/correct/example_completeness_and_stereotypes.sd");
		ASTObjectDeclaration od;

		od = sd.getSequenceDiagram().getObjectDeclarations().get(0);
		assertFalse(od.getSDCompleteness().isPresent());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(1);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertTrue(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(2);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertTrue(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(3);
		assertTrue(od.getSDCompleteness().isPresent());
		assertTrue(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(4);
		assertTrue(od.getSDCompleteness().isPresent());
		assertTrue(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(5);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertTrue(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(6);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertTrue(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

	}

}
