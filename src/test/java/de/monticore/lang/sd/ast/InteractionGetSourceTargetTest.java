package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class InteractionGetSourceTargetTest {

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
		ASTSDArtifact sd = loadModel("src/test/resources/examples/ast/SourceTargetTest.sd");
		List<ASTSDElement> elements = sd.getSequenceDiagram().getSDElements();

		ASTMethodCall e1 = (ASTMethodCall) elements.get(0).getInteraction().get();
		assertEquals("a", e1.getSource().getName().get());
		assertEquals("b", e1.getTarget().getName().get());

		ASTMethodCall e2 = (ASTMethodCall) elements.get(1).getInteraction().get();
		assertEquals("b", e2.getSource().getName().get());
		assertEquals("a", e2.getTarget().getName().get());

		ASTReturn e3 = (ASTReturn) elements.get(2).getInteraction().get();
		assertEquals("a", e3.getSource().getName().get());
		assertEquals("b", e3.getTarget().getName().get());

		ASTReturn e4 = (ASTReturn) elements.get(3).getInteraction().get();
		assertEquals("b", e4.getSource().getName().get());
		assertEquals("a", e4.getTarget().getName().get());

		ASTException e5 = (ASTException) elements.get(4).getInteraction().get();
		assertEquals("a", e5.getSource().getName().get());
		assertEquals("b", e5.getTarget().getName().get());

		ASTException e6 = (ASTException) elements.get(5).getInteraction().get();
		assertEquals("b", e6.getSource().getName().get());
		assertEquals("a", e6.getTarget().getName().get());
	}

}
