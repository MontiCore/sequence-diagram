package de.monticore.lang.sd.generators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import de.monticore.generating.GeneratorEngine;
import de.monticore.generating.GeneratorSetup;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

public class HtmlGenerator {

	private final static String LOGGER_NAME = HtmlGenerator.class.getName();

	public static void generate(ASTSequenceDiagram ast, File outputDir) {

		// Target dir
		GeneratorEngine generator = createGeneratorEngine(outputDir);
		String name = ast.getName();
		String outputPath = name.toLowerCase() + "/";

		// Some processing

		// Generation
		Path outputFile = Paths.get(outputPath, ast.getName() + ".html");
		generator.generate("html.sequencediagram.ftl", outputFile, ast);
		Log.trace(LOGGER_NAME, "Generated html diagram for sd " + ast.getName() + " to " + outputDir.getAbsolutePath());

		// Dependencies
		String dependencies = "src/main/resources/html/";
		copyFile(Paths.get(dependencies + "white.png"), Paths.get("gen/" + outputPath + "white.png"));
		copyFile(Paths.get(dependencies + "corner.png"), Paths.get("gen/" + outputPath + "corner.png"));
		copyFile(Paths.get(dependencies + "head.png"), Paths.get("gen/" + outputPath + "head.png"));

	}

	private static void copyFile(Path in, Path out) {
		try {
			Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			Log.error("Could not copy resource file.");
		}
	}

	private static GeneratorEngine createGeneratorEngine(File outputDirectory) {
		final GeneratorSetup setup = new GeneratorSetup(outputDirectory);
		setup.setCommentStart(Optional.of("<!--"));
		setup.setCommentEnd(Optional.of("-->"));
		return new GeneratorEngine(setup);
	}

}
