package de.monticore.lang.sd4java._parser;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class SD4JavaParser extends SD4JavaParserTOP {

  @Override
  public Optional<ASTSDArtifact> parse(String fileName) throws IOException {
    Optional<ASTSDArtifact> sd = parseSDArtifact(fileName);
    sd.ifPresent(e -> e.setFilePath(Paths.get(fileName)));
    return sd;
  }
}
