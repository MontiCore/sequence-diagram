/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class CorrectObjectConstructionTypesCoco implements SD4DevelopmentASTSDNewCoCo {

  static final String MESSAGE_ERROR_INCOMPATIBLE_TYPES = CorrectObjectConstructionTypesCoco.class.getSimpleName() + ": " +
          "Incompatible types. Required %s but found %s.";

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public CorrectObjectConstructionTypesCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDNew node) { //TODO
    if(!node.getDeclarationType().deepEquals(node.getInitializationType())) {
      String declarationType = node.getDeclarationType().printType(prettyPrinter);
      String initializationType = node.getInitializationType().printType(prettyPrinter);
      Log.error(String.format(MESSAGE_ERROR_INCOMPATIBLE_TYPES, declarationType, initializationType));
    }
  }
}
