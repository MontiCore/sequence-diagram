package de.monticore.lang.sd4development._parser;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class SD4DevelopmentParser extends SD4DevelopmentParserTOP {

  @Override
  public Optional<ASTSDArtifact> parse(String fileName) throws IOException {
    Optional<ASTSDArtifact> sd = parseSDArtifact(fileName);
    sd.ifPresent(e -> e.setFilePath(Paths.get(fileName)));
    return sd;
  }
}
