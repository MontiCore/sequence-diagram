/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator;

import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

public abstract class SDGenerator {

  public SDData transform(ASTSDArtifact ast, GlobalExtensionManagement glex){
    return null;
  }
  public SDData transform(ASTSDArtifact ast, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex){
    return null;
  }

}
