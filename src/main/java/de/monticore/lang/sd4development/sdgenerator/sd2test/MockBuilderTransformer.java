/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.MCTypeFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockBuilderTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact sdArtifact) {
    List<ASTCDElement> mockBuilders = new ArrayList<>();

    for (TypeSymbol type : scope.getTypeSymbols().values()) {
      if(type.getName().endsWith("Mill") ||type.getName().endsWith("Builder")) {
        continue;
      }
      mockBuilders.add(createMockBuilders(sdArtifact, type));
    }

    for (OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if(type.getName().endsWith("Mill") ||type.getName().endsWith("Builder")) {
        continue;
      }
      mockBuilders.add(createMockBuilders(sdArtifact, type));
    }

    classes.addAll(mockBuilders);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(mockBuilders);
  }

  private ASTCDClass createMockBuilders(ASTSDArtifact astsdArtifact, TypeSymbol type) {

    String builderType = "Mock" + type.getName() + "Builder";

    String sdName = astsdArtifact.getSequenceDiagram().getName();

    ASTCDClass mockBuilder = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(builderType)
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance()
          .createQualifiedType(type.getName() + "Builder"))
        .build())
      .build();

    String sb = "protected " + builderType + " " + "realBuilder ;";
    cd4C.addAttribute(mockBuilder, sb);

    List<FieldSymbol> fieldList = ((ISD4DevelopmentArtifactScope) type.getSpannedScope())
      .getLocalFieldSymbols()
      .stream()
      .filter(a -> !(a.getType().isPrimitive()))
      .collect(Collectors.toList());

    List<VariableSymbol> variableList = type.getSpannedScope().getLocalVariableSymbols()
      .stream()
      .filter(v -> !(v.getType().isPrimitive()))
      .collect(Collectors.toList());

    List<FieldSymbol> assignFieldList = ((ISD4DevelopmentArtifactScope)type.getSpannedScope()).getLocalFieldSymbols();

    List<VariableSymbol> assignVariableList = type.getSpannedScope().getLocalVariableSymbols();

    cd4C.addConstructor(mockBuilder, "sdgenerator.sd2test.BuilderConstructor",
      builderType);
    cd4C.addMethod(mockBuilder, "sdgenerator.sd2test.BuilderBuildMethods",
      type.getName(), fieldList, variableList, false, sdName, assignFieldList, assignVariableList);
    cd4C.addMethod(mockBuilder, "sdgenerator.sd2test.BuilderBuildMethods",
      type.getName(), fieldList, variableList, true, sdName, assignFieldList, assignVariableList);

    return mockBuilder;
  }

  public MockBuilderTransformer(ASTCDCompilationUnit compilationUnit,
                                List<ASTCDElement> classes,
                                ISD4DevelopmentArtifactScope scope,
                                GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
