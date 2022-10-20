package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.cd.facade.MCQualifiedNameFacade;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;

import java.util.ArrayList;
import java.util.List;

public class ExampleProdClass extends AbstractVisitor {

  public ExampleProdClass(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, glex);
  }

  @Override
  public void visit(ASTSDArtifact ast) {
    List<ASTCDElement> cdClasses = new ArrayList<>();

    cdClasses.add(provideBookClass());
    cdClasses.add(provideCustomerClass());
    cdClasses.add(provideInventoryManagerClass());
    cdClasses.add(providePaymentGatewayClass());
    cdClasses.add(provideStoreClass());
    cdClasses.add(provideMainMill());

    ASTCDPackage pkg = CD4CodeMill.cDPackageBuilder()
        .setMCQualifiedName(MCQualifiedNameFacade
          .createQualifiedName("com.example"))
        .build();
    
    pkg.addAllCDElements(cdClasses);
    classes.addAll(cdClasses);
    
    compilationUnit.getCDDefinition().addCDElement(pkg);
  }

  public ASTCDElement provideBookClass() {

    //Book Class
    ASTCDClass bookClass = new ASTCDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("ASTBook")
      .build();

    cd4C.addConstructor(bookClass, "sdgenerator.sd2java.DefaultConstructor", "ASTBook");
    String quantity = "public int quantity;";
    String price = "public int price;";
    String bookId = "public String bookId;";

    cd4C.addAttribute(bookClass, true, true, quantity);
    cd4C.addAttribute(bookClass, true, true, price);
    cd4C.addAttribute(bookClass, true, true, bookId);

    return bookClass;
  }

  public ASTCDElement provideCustomerClass() {
    // Customer class
    ASTCDClass customerClass = new ASTCDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("ASTCustomer")
      .build();
    cd4C.addConstructor(customerClass, "sdgenerator.sd2java.DefaultConstructor", "ASTCustomer");
    String age = "protected  int age;";
    String accountBalance = "protected  int accountBalance ;";
    String customerID = "protected  String customerID ;";
    String name = "protected  String name ;";
    String email = "protected  String email ;";
    String password = "protected  String password ;";

    cd4C.addAttribute(customerClass, true, true, age);
    cd4C.addAttribute(customerClass, true, true, accountBalance);
    cd4C.addAttribute(customerClass, true, true, customerID);
    cd4C.addAttribute(customerClass, true, true, name);
    cd4C.addAttribute(customerClass, true, true, email);
    cd4C.addAttribute(customerClass, true, true, password);

    return customerClass;
  }

  public ASTCDElement provideInventoryManagerClass() {
    //Inventory Manager
    ASTCDClass inventoryManagerClass = new ASTCDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("ASTInventoryManager")
      .build();

    cd4C.addConstructor(inventoryManagerClass, "sdgenerator.sd2java.DefaultConstructor", "ASTInventoryManager");

    cd4C.addMethod(inventoryManagerClass, "sdgenerator.sd2test.InventoryManagerMethods", "checkQuantity");
    cd4C.addMethod(inventoryManagerClass, "sdgenerator.sd2test.InventoryManagerMethods", "addInventory");
    cd4C.addMethod(inventoryManagerClass, "sdgenerator.sd2test.InventoryManagerMethods", "removeInventory");

    return inventoryManagerClass;
  }

  public ASTCDElement providePaymentGatewayClass() {
    ASTCDClass paymentGatewayClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("ASTPaymentGateway")
      .build();
    cd4C.addConstructor(paymentGatewayClass, "sdgenerator.sd2java.DefaultConstructor", "ASTPaymentGateway");
    cd4C.addMethod(paymentGatewayClass, "sdgenerator.sd2test.PaymentGatewayMethods", "processPayment");
    cd4C.addMethod(paymentGatewayClass, "sdgenerator.sd2test.PaymentGatewayMethods", "processReturn");


    return paymentGatewayClass;
  }

  public ASTCDElement provideStoreClass() {
    ASTCDClass storeClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("ASTStore")
      .build();
    String storeCtor = "public ASTStore()";
    cd4C.addConstructor(storeClass, "sdgenerator.sd2java.DefaultConstructor", "ASTStore");

    String inventoryManagerAttribute = "protected  ASTInventoryManager inventoryManager;";
    String paymentGatewayAttribute = "protected  ASTPaymentGateway paymentGateway;";

    cd4C.addAttribute(storeClass, true, true, inventoryManagerAttribute);
    cd4C.addAttribute(storeClass, true, true, paymentGatewayAttribute);

    cd4C.addMethod(storeClass, "sdgenerator.sd2test.StoreMethods", "checkAvailability");
    cd4C.addMethod(storeClass, "sdgenerator.sd2test.StoreMethods", "makePurchase");
    cd4C.addMethod(storeClass, "sdgenerator.sd2test.StoreMethods", "placeReturn");

    return storeClass;
  }

  public ASTCDElement provideMainMill() {
    ASTCDClass mainMill = new ASTCDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("BookstoreMill")
      .build();
    cd4C.addConstructor(mainMill, "sdgenerator.sd2java.DefaultConstructor", "BookstoreMill");

    String millAttribute = "protected  static  BookstoreMill mill ;";
    String bookAttribute = "protected  static  BookstoreMill millASTBookBuilder ;";
    String customerAttribute = "protected  static  BookstoreMill millASTCustomerBuilder ;";
    String inventoryManagerAttribute = "protected  static  BookstoreMill millASTInventoryManagerBuilder ;";
    String paymentGatewayAttribute = "protected  static  BookstoreMill millASTPaymentGatewayBuilder ;";
    String storeAttribute = "protected  static  BookstoreMill millASTStoreBuilder ;";

    cd4C.addAttribute(mainMill, millAttribute);
    cd4C.addAttribute(mainMill, bookAttribute);
    cd4C.addAttribute(mainMill, customerAttribute);
    cd4C.addAttribute(mainMill, inventoryManagerAttribute);
    cd4C.addAttribute(mainMill, paymentGatewayAttribute);
    cd4C.addAttribute(mainMill, storeAttribute);

    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillMethods", "book");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillMethods", "customer");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillMethods", "inventoryManager");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillMethods", "paymentGateway");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillMethods", "store");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillSecondaryMethods", "book");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillSecondaryMethods", "customer");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillSecondaryMethods", "inventoryManager");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillSecondaryMethods", "paymentGateway");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillSecondaryMethods", "store");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillCommonMethods", "initMe");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillCommonMethods", "reset");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillCommonMethods", "getMill");
    cd4C.addMethod(mainMill, "sdgenerator.sd2test.MillCommonMethods", "init");

    return mainMill;
  }

}
