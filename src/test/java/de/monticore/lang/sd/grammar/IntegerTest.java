package de.monticore.lang.sd.grammar;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.monticore.java.prettyprint.JavaDSLPrettyPrinter;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

public class IntegerTest {

	@BeforeClass
	public static void setUp() {
		Log.enableFailQuick(true);
	}

	@Test
	public void testExample() throws IOException {
		// Load model and check if integer gets parsed
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "integer.sd");

		// Assertion
		ASTSDElement el = sd.getSequenceDiagram().getSDElements().get(0);
		JavaDSLPrettyPrinter pp = new JavaDSLPrettyPrinter(new IndentPrinter());
		String java = pp.prettyprint(el.getSDJava().get().getBlockStatement().getBlockStatements().get(0));
		assertEquals("value=1;\n", java);
	}

}
