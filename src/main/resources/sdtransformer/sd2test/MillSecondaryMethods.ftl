<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "book">
${cd4c.method(" public static ASTBookBuilder _bookBuilder ()")}
                 return new ASTBookBuilder();

 <#elseif methodName == "customer">
   ${cd4c.method(" public static ASTCustomerBuilder _customerBuilder ()")}
                    return new ASTCustomerBuilder();

 <#elseif methodName == "inventoryManager">
      ${cd4c.method(" public static ASTInventoryManagerBuilder _inventoryManagerBuilder ()")}
                       return new ASTInventoryManagerBuilder();
   <#elseif methodName == "paymentGateway">
           ${cd4c.method(" public static ASTPaymentGatewayBuilder _paymentGatewayBuilder ()")}
                            return new ASTPaymentGatewayBuilder();
      <#elseif methodName == "store">

                    ${cd4c.method(" public static ASTStoreBuilder _storeBuilder ()")}
                                     return new ASTStoreBuilder();
 </#if>
