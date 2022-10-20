package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4code._visitor.CD4CodeTraverser;
import de.monticore.cd4codebasis._ast.ASTCDMethod;
import de.monticore.cd4codebasis._ast.ASTCDParameter;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes.MCBasicTypesMill;
import de.monticore.types.prettyprint.MCArrayTypesPrettyPrinter;
import de.monticore.types.prettyprint.MCSimpleGenericTypesFullPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class MockClassTransformer extends AbstractVisitor implements SDBasisVisitor2 {

  public MockClassTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, glex);
  }

  @Override
  public void visit(ASTSDArtifact ast) {

    List<ASTCDElement> mockClasses = new ArrayList<>();

    for(ASTCDElement cdElement: compilationUnit.getCDDefinition().getCDPackagesList().get(0).getCDElementList()) {
      ASTCDClass cdClass;
      if(cdElement instanceof ASTCDClass) {
        cdClass = (ASTCDClass) cdElement;
      } else {
        continue;
      }

      if(cdClass.getName().endsWith("Mill") || cdClass.getName().endsWith("Builder")) {
        continue;
      }
      mockClasses.add(createMockClass(ast, cdClass));
    }

    classes.addAll(mockClasses);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(mockClasses);
  }

  private ASTCDClass createMockClass(ASTSDArtifact ast, ASTCDClass astcdClass) {
    String sdName = ast.getSequenceDiagram().getName();
    String monitorTemplate = "protected " + sdName + "Monitor " + uncapitalize(sdName) + "Monitor";
    ASTCDClass mockClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("Mock" + astcdClass.getName())
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(astcdClass.getName())).build())
      .build();
    cd4C.addAttribute(mockClass, false, true, monitorTemplate);
    addMockMethods(mockClass, astcdClass, ast);
    return mockClass;
  }

  private void addMockMethods(ASTCDClass mockClass, ASTCDClass astcdClass, ASTSDArtifact ast) {

    String target = "";
    String monitorName = uncapitalize(ast.getSequenceDiagram().getName()) + "Monitor";

    for(ASTCDMethod prodMethod: astcdClass.getCDMethodList()) {
      boolean isSDMethod = false;
      for(ASTSDElement sdElement: ast.getSequenceDiagram().getSDBody().getSDElementList()) {
        ASTSDAction action;
        if(sdElement instanceof ASTSDSendMessage) {
           action = ((ASTSDSendMessage) sdElement).getSDAction();
        } else {
          continue;
        }
        if(action instanceof ASTSDCall) {
          if(((ASTSDCall) action).getName().equals(prodMethod.getName())) {
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
      String prodReturnType = prodMethod.getMCReturnType().printType(simplePrinter);
      String prodMethodName = prodMethod.getName();
      List<ASTCDParameter> prodMethodCDParameterList = prodMethod.getCDParameterList();
      if(!prodMethodCDParameterList.isEmpty()) {
        List<String> parameterList = new ArrayList<>();
        for(ASTCDParameter pp: prodMethodCDParameterList) {
          parameterList.add(pp.getName());
        }
        //parameter list in a string format to print in the template
        parameterString = join(parameterList);
      }
      cd4C.addMethod(mockClass, "sdgenerator.sd2test.MockMethods",
        prodReturnType, prodMethodName, prodMethodCDParameterList, parameterString,
        monitorName, capitalize(target), capitalize(prodMethodName), isSDMethod);
    }
  }
}
