/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdtransformer;

import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

public interface SDTransformer {

  SDData transform(ASTSDArtifact ast, GlobalExtensionManagement glex);

}
