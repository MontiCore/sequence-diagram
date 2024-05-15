/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._parser;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class SD4ComponentsParser extends SD4ComponentsParserTOP {

  @Override
  public Optional<ASTSDArtifact> parse(String fileName) throws IOException {
    // use parser as usual and save file path in artifact
    Optional<ASTSDArtifact> sd = parseSDArtifact(fileName);
    sd.ifPresent(e -> e.setFilePath(Paths.get(fileName)));
    return sd;
  }
}
