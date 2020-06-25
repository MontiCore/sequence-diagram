/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sdbasis._cocos.ObjectNameNamingConventionCoco;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

@Deprecated
public class CorrectObjectConstructionNamingCoco implements SD4DevelopmentASTSDNewCoCo {

  static final String MESSAGE_WARNING_LOWER_CASE = CorrectObjectConstructionNamingCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention";

  static final String MESSAGE_WARNING_UPPER_CASE = CorrectObjectConstructionNamingCoco.class.getSimpleName() + ": "
          + "Class %s of object declaration "
          + "should be written upper case by convention.";

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public CorrectObjectConstructionNamingCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDNew node) {

    String declarationType = node.getDeclarationType().printType(prettyPrinter);
    String initializationType = node.getInitializationType().printType(prettyPrinter);

    if (Character.isLowerCase(declarationType.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, declarationType));
    }
    if (Character.isLowerCase(initializationType.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, initializationType));
    }
    if (Character.isUpperCase(node.getName().charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, node.getName()));
    }
  }
}
