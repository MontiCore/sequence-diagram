package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4code._visitor.CD4CodeTraverser;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes.MCBasicTypesMill;
import de.monticore.types.prettyprint.MCArrayTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesFullPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class MockClassTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact ast) {
    List<ASTCDElement> mockClasses = new ArrayList<>();

    for(TypeSymbol type : scope.getTypeSymbols().values()) {
      if(type.getName().endsWith("Mill") || type.getName().endsWith("Builder")) {
        continue;
      }
      mockClasses.add(createMockClass(ast, type));
    }
    for(OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if(type.getName().endsWith("Mill") || type.getName().endsWith("Builder")) {
        continue;
      }
      mockClasses.add(createMockClass(ast, type));
    }

    classes.addAll(mockClasses);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(mockClasses);
  }

  private ASTCDClass createMockClass(ASTSDArtifact ast, TypeSymbol type) {
    String sdName = ast.getSequenceDiagram().getName();
    String monitorTemplate = "protected " + sdName + "Monitor " + uncapitalize(sdName) + "Monitor";
    ASTCDClass mockClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("Mock" + type.getName())
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(type.getName())).build())
      .build();
    cd4C.addAttribute(mockClass, false, true, monitorTemplate);
    addMockMethods(mockClass, type, ast);
    return mockClass;
  }

  private void addMockMethods(ASTCDClass mockClass, TypeSymbol type, ASTSDArtifact ast) {

    String target = "";
    String monitorName = uncapitalize(ast.getSequenceDiagram().getName()) + "Monitor";

    for(FunctionSymbol function: type.getFunctionList()) {
      boolean isSDMethod = false;
      for(ASTSDElement sdElement: ast.getSequenceDiagram().getSDBody().getSDElementList()) {
        ASTSDAction action;
        if(sdElement instanceof ASTSDSendMessage) {
           action = ((ASTSDSendMessage) sdElement).getSDAction();
        } else {
          continue;
        }
        if(action instanceof ASTSDCall) {
          if(((ASTSDCall) action).getName().equals(function.getName())) {
            isSDMethod = true;
            ASTSDTarget targetObject;
            if(((ASTSDSendMessage) sdElement).isPresentSDTarget()) {
              targetObject = ((ASTSDSendMessage) sdElement).getSDTarget();
            } else {
              //Creating a dummy target object when there isn't one
              targetObject = SD4DevelopmentMill.sDObjectTargetBuilder().setName("Target").build();
            }
            if(targetObject instanceof ASTSDClass) {
              target = ((ASTSDClass) targetObject).getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter());
            } else if(targetObject instanceof ASTSDObjectTarget) {
              target = ((ASTSDObjectTarget) targetObject).getName();
            } else {
              Log.error("0xA4388 Unknown ASTSDTarget implementation: " + targetObject.getClass().getName());
            }
          }
        }
      }
      String parameterString = "";
      //initialize required printers
      MCSimpleGenericTypesFullPrettyPrinter simplePrinter = new MCSimpleGenericTypesFullPrettyPrinter(new IndentPrinter());
      MCArrayTypesPrettyPrinter arrayPrinter = new MCArrayTypesPrettyPrinter(simplePrinter.getPrinter());
      ((CD4CodeTraverser) simplePrinter.getTraverser()).add4MCArrayTypes(arrayPrinter);
      ((CD4CodeTraverser) simplePrinter.getTraverser()).setMCArrayTypesHandler(arrayPrinter);

      //Get return type, method name and list of parameters
      String prodReturnType = function.getType().print();
      String prodMethodName = function.getName();
      List<VariableSymbol> prodMethodCDParameterList = function.getParameterList();
      if(!prodMethodCDParameterList.isEmpty()) {
        List<String> parameterList = new ArrayList<>();
        for(VariableSymbol symbol: prodMethodCDParameterList) {
          parameterList.add(symbol.getName());
        }
        //parameter list in a string format to print in the template
        parameterString = join(parameterList);
      }
      cd4C.addMethod(mockClass, "sdgenerator.sd2test.MockMethods",
        prodReturnType, prodMethodName, prodMethodCDParameterList, parameterString,
        monitorName, capitalize(target), capitalize(prodMethodName), isSDMethod);
    }
  }

  public MockClassTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
