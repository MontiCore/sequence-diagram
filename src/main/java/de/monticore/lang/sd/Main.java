package de.monticore.lang.sd;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import de.monticore.ast.ASTNode;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._parser.SDParser;
import de.monticore.lang.sd.generator.HtmlGenerator;
import de.se_rwth.commons.logging.Log;

public class Main {

	private final static String MODEL_PATH = "src/test/resources/examples/correct/example.sd";
	private final static String LOGGER_NAME = Main.class.getName();

	public static void main(String[] args) {

		// 1-) Parse the model
		Optional<ASTSequenceDiagram> sd = parse(MODEL_PATH);
		if (!sd.isPresent()) {
			return;
		}
		ASTSequenceDiagram ast = sd.get();

		// 2-) Generate SymTab
		// TODO

		// 3-) Check CoCos
		// TODO

		// 4-) Generate End product
		generate(ast);
	}

	private static Optional<ASTSequenceDiagram> parse(String modelPath) {
		SDParser parser = new SDParser();
		Path model = Paths.get(modelPath);
		ASTSequenceDiagram ast = null;
		try {
			Optional<? extends ASTNode> optAst = parser.parseSequenceDiagram(model.toString());
			if (optAst.isPresent() && (optAst.get() instanceof ASTSequenceDiagram)) {
				ast = (ASTSequenceDiagram) optAst.get();
			}
		} catch (Exception e) {
			Log.error("Could not parse model " + modelPath);
		}
		return Optional.ofNullable(ast);
	}

	private static void generate(ASTSequenceDiagram ast) {
		String outDir = "gen";
		Log.info("GENERATOR OUTPUT DIR := " + outDir, LOGGER_NAME);
		HtmlGenerator.generate(ast, new File(outDir));
	}

}
