package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class SDDiffTestBase {

  private static final String BASE_PATH = Paths.get("src", "test", "resources", "sddiff").toString();

  private final SD4DevelopmentParser parser = new SD4DevelopmentParser();;

  ASTSDArtifact parse(String model) {
    try {
      return parser.parse(BASE_PATH + "/" + model + ".sd").orElseThrow(NoSuchElementException::new);
    } catch (IOException | NoSuchElementException e) {
      e.printStackTrace();
    }
    System.exit(1);
    throw new IllegalStateException("Something went wrong..");
  }
}
