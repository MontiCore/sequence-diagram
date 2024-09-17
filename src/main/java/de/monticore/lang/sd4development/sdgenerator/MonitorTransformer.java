/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4codebasis._ast.ASTCDMethod;
import de.monticore.cdbasis._ast.ASTCDAttribute;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.cdinterfaceandenum._ast.ASTCDEnum;
import de.monticore.cdinterfaceandenum._ast.ASTCDEnumConstant;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpressionBuilder;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.StringHookPoint;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sd4development._ast.ASTSDReturn;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverserImplementation;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.literals.mccommonliterals._prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonitorTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSequenceDiagram sequenceDiagram) {
    ASTSDBody sdBody = sequenceDiagram.getSDBody();

    String monitorName = capitalize(sequenceDiagram.getName() + "Monitor");

    ASTCDClass cdClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(monitorName)
      .build();


    String monitorNameAttribute = "private static final String monitorName = \" " + monitorName + "\"";
    cd4C.addAttribute(cdClass, true, false, monitorNameAttribute);

    ASTCDEnum stateEnum = createStateEnum(glex, monitorName);
    List<ASTCDEnumConstant> stateEnumConstants = new ArrayList<>();
    List<String> callStack = new ArrayList<>();

    for(ASTSDElement sdElement: sdBody.getSDElementList()) {
      boolean isFirst = false;
      ASTSDAction action;
      if(sdElement instanceof ASTSDSendMessage) {
        action = ((ASTSDSendMessage) sdElement).getSDAction();
      } else {
        continue;
      }
      if(action instanceof ASTSDCall) {
        if(callStack.size() == 0) {
          isFirst = true;
        }
        ASTSDCall call = (ASTSDCall) action;
        String callee = call.getName();
        stateEnumConstants.addAll(addEnumElement(cdClass, callee, isFirst, monitorName, true));
        callStack.add(((ASTSDCall) action).getName());
        createBeginMonitorMethods(cdClass, sdElement, monitorName);
      }
      if(action instanceof ASTSDReturn) {
        if(callStack.size() < 1) {
          Log.error("Call does not precede return");
        }
        String call = callStack.remove(callStack.size() - 1);
        stateEnumConstants.addAll(addEnumElement(cdClass, call, false, monitorName, false));
        createEndMonitorMethods(cdClass, sdElement, call, monitorName);
      }
    }
    stateEnumConstants.add(CD4CodeMill.cD4CodeEnumConstantBuilder().setName("END").build());
    stateEnum.addAllCDEnumConstants(stateEnumConstants);

    classes.add(cdClass);
    classes.add(stateEnum);

    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addCDElement(cdClass);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addCDElement(stateEnum);
  }

  private ASTCDEnum createStateEnum(GlobalExtensionManagement glex, String monitorName) {
    List<ASTCDEnumConstant> enumElements = new ArrayList<>();

    ASTCDEnum stateEnum = CD4CodeMill.cDEnumBuilder()
      .setName(monitorName + "State")
      .addAllCDEnumConstants(enumElements)
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .build();

    ASTExpression expression = new ASTNameExpressionBuilder().setName("values()").build();
    ASTCDAttribute vals = CD4CodeMill.cDAttributeBuilder()
      .setName("vals")
      .setInitial(expression)
      .setMCType(CD4CodeMill.mCQualifiedTypeBuilder()
        .setMCQualifiedName(CD4CodeMill.mCQualifiedNameBuilder()
          .addParts(monitorName + "State[]")
          .build())
        .build())
      .setModifier(CD4CodeMill.modifierBuilder().STATIC().PUBLIC().build())
      .build();
    ASTCDMethod nextMethod = CD4CodeMill.cDMethodBuilder()
      .setName("next")
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setMCReturnType(CD4CodeMill.mCReturnTypeBuilder()
        .setMCType(CD4CodeMill.mCQualifiedTypeBuilder()
          .setMCQualifiedName(CD4CodeMill.mCQualifiedNameBuilder()
            .addParts(monitorName + "State")
            .build())
          .build())
        .build())
      .build();

    String nextMethodContent = " return vals[(this.ordinal()+1) % vals.length];";
    glex.replaceTemplate("cd2java.EmptyBody", nextMethod, new StringHookPoint(nextMethodContent));
    stateEnum.addCDMember(nextMethod);
    stateEnum.addCDMember(vals);

    return stateEnum;
  }

  private List<ASTCDEnumConstant> addEnumElement(ASTCDClass cdClass, String callName, Boolean isFirst, String monitorName, Boolean isCall) {
    List<ASTCDEnumConstant> enumElements = new ArrayList<>();
    if(isCall) {
      enumElements.add(CD4CodeMill.cD4CodeEnumConstantBuilder().setName("CALL_" + capitalize(callName)).build());
    } else {
      enumElements.add(CD4CodeMill.cD4CodeEnumConstantBuilder().setName("RETURN_" + capitalize(callName)).build());
    }

    if(isFirst) {
      String monitorStateAttribute = "private " + monitorName + "State currentState = " + monitorName + "State.CALL_" + capitalize(callName) + ";";
      CD4C.getInstance().addAttribute(cdClass, monitorStateAttribute);
    }

    return enumElements;
  }

  private void createBeginMonitorMethods(ASTCDClass cdClass, ASTSDElement sdElement, String monitorName) {
    ASTSDSendMessage sendMessage = (ASTSDSendMessage) sdElement;
    ASTSDTarget targetObject;

    if(sendMessage.isPresentSDTarget()) {
      targetObject = sendMessage.getSDTarget();
    } else {
      //Creating a dummy target object when there isn't one
      targetObject = new ASTSDObjectTargetBuilder().setName("Target").build();
    }

    String target = "";
    if(targetObject instanceof ASTSDClass) {
      target = SD4DevelopmentMill.prettyPrint((((ASTSDClass) targetObject).getMCObjectType()), false);
    } else if(targetObject instanceof ASTSDObjectTarget) {
      target = ((ASTSDObjectTarget) targetObject).getName();
    } else {
      Log.error("Unknown ASTSDTarget implementation: " + targetObject.getClass().getName());
    }

    ASTSDAction action = sendMessage.getSDAction();

    List<VariableSymbol> methodParameters = new ArrayList<>();
    List<Parameter> parameterList = new ArrayList<>();
    ASTSDCall call = (ASTSDCall) action;
    SD4DevelopmentTraverser t = new SD4DevelopmentTraverserImplementation();
    CustomPrinter ip = new CustomPrinter();
    MCCommonLiteralsPrettyPrinter lp = new MCCommonLiteralsPrettyPrinter(ip, true);
    t.add4MCCommonLiterals(lp);
    t.setMCCommonLiteralsHandler(lp);
    action.accept(t);
    String paramValue = ip.getContent();
    List<String> paramValues = Arrays.asList(paramValue.split(","));

    String callee = call.getName();
    for(TypeSymbol type: scope.getTypeSymbols().values()) {
      if(type.getName().equalsIgnoreCase(target)) {
        List<FunctionSymbol> methodList = type.getFunctionList();
        for(FunctionSymbol m: methodList) {
          if((m.getName()).equals(callee)) {
            methodParameters = m.getParameterList();
            for(int i = 0; i < m.getParameterList().size(); i++) {
              parameterList.add(new Parameter(m.getParameterList().get(i), call.getArguments().getExpression(i)));
            }
          }
        }
      }
    }
    for(OOTypeSymbol type: scope.getOOTypeSymbols().values()) {
      if(type.getName().equalsIgnoreCase(target)) {
        List<FunctionSymbol> methodList = type.getFunctionList();
        for(FunctionSymbol m: methodList) {
          if((m.getName()).equals(callee)) {
            methodParameters = m.getParameterList();
            for(int i = 0; i < m.getParameterList().size(); i++) {
              parameterList.add(new Parameter(m.getParameterList().get(i), call.getArguments().getExpression(i)));
            }
          }
        }
      }
    }
    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorBeginMethods",
      capitalize(target), capitalize(callee), methodParameters, parameterList, paramValues, monitorName);
  }

  private void createEndMonitorMethods(ASTCDClass cdClass, ASTSDElement sdElement, String call, String monitorName) {

    ASTSDSendMessage sendMessage = (ASTSDSendMessage) sdElement;
    ASTSDSource targetObject;

    if(sendMessage.isPresentSDSource()) {
      targetObject = sendMessage.getSDSource();
    } else {
      //Creating a dummy target object when there isn't one
      targetObject = SD4DevelopmentMill.sDObjectSourceBuilder().setName("Target").build();
    }

    String target = "";
    if(targetObject instanceof ASTSDClass) {
      target = SD4DevelopmentMill.prettyPrint(((ASTSDClass) targetObject).getMCObjectType(), false);
    } else if(targetObject instanceof ASTSDObjectSource) {
      target = ((ASTSDObjectSource) targetObject).getName();
    } else {
      Log.error("Unknown ASTSDTarget implementation: " + targetObject.getClass().getName());
    }

    ASTSDAction action = sendMessage.getSDAction();
    SD4DevelopmentTraverser t = new SD4DevelopmentTraverserImplementation();
    IndentPrinter ip = new IndentPrinter();
    MCCommonLiteralsPrettyPrinter lp = new MCCommonLiteralsPrettyPrinter(ip, true);
    t.add4MCCommonLiterals(lp);
    t.setMCCommonLiteralsHandler(lp);
    action.accept(t);
    String returnValue = ip.getContent();
    String returnType = "";

    for(TypeSymbol type: scope.getTypeSymbols().values()) {
      List<FunctionSymbol> methodList = type.getFunctionList();
      for(FunctionSymbol m: methodList) {
        if((m.getName()).equals(call)) {
          returnType = m.getType().print();
        }
      }
    }
    for(OOTypeSymbol type: scope.getOOTypeSymbols().values()) {
      List<FunctionSymbol> methodList = type.getFunctionList();
      for(FunctionSymbol m: methodList) {
        if((m.getName()).equals(call)) {
          returnType = m.getType().print();
        }
      }
    }

    if(returnValue.isEmpty()) {
      cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorLogEndEmpty", capitalize(target), capitalize(call), monitorName);
    } else {
      cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorLogEndparameters", capitalize(target), capitalize(call), returnValue, returnType, monitorName);
    }
  }

  public MonitorTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
