/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development.sdgenerator.SDGenerator;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;

import java.util.ArrayList;
import java.util.List;

public class SD2TestGenerator implements SDGenerator {

  public SD2TestData transform(ASTSDArtifact ast, GlobalExtensionManagement glex) {

    List<ASTCDElement> classes = new ArrayList<>();

    AbstractVisitor visitor = new MonitorInterfaceVisitor(classes, glex);
    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new ExampleProdClass(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new MainMillVisitor(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new MonitorTransformer(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new MockBuilderTransformer(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new MockClassTransformer(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    visitor = new MonitorMillVisitor(visitor.getCompilationUnit(), visitor.getClasses(), glex);
    traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    traverser.handle(ast);

    return new SD2TestData(visitor.getCompilationUnit(), visitor.getClasses());
  }

}
