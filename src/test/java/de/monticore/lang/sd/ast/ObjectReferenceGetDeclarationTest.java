package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import de.monticore.ast.ASTNode;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._parser.SDParser;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.monticore.lang.sd._symboltable.SDSymbolTableCreator;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.se_rwth.commons.logging.Log;

public class ObjectReferenceGetDeclarationTest {

	private ASTSDArtifact loadModel(String path, String model) {

		SDLanguage lang = new SDLanguage();
		ResolvingConfiguration config = new ResolvingConfiguration();
		config.addDefaultFilters(lang.getResolvers());

		// Parse model
		SDParser parser = lang.getParser();
		ASTSDArtifact ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSDArtifact(path + "/" + model);
			if (optAst.isPresent() && (optAst.get() instanceof ASTSDArtifact)) {
				ast = (ASTSDArtifact) optAst.get();
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

	@Test
	public void test() throws IOException {
		ASTSDArtifact sd = loadModel("src/test/resources/examples/ast", "ObjectReferenceTest.sd");
		ASTInteraction i1 = sd.getSequenceDiagram().getSDElements().get(0).getInteraction().get();
		ASTObjectReference or1 = ((ASTMethodCall) i1).getTarget();
		ASTInteraction i2 = sd.getSequenceDiagram().getSDElements().get(1).getInteraction().get();
		ASTObjectReference or2 = ((ASTMethodCall) i2).getTarget();

		assertEquals("c", or2.getDeclaration().getName().get());
		assertEquals("C", or2.getDeclaration().getOfType().get());

		assertEquals("b", or1.getDeclaration().getName().get());
		assertEquals("B", or1.getDeclaration().getOfType().get());

	}

}
