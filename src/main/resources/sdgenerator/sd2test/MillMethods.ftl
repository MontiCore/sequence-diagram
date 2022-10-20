<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "book">
${cd4c.method(" public static ASTBookBuilder bookBuilder ()")}
                 if (millASTBookBuilder == null) {
                   millASTBookBuilder = getMill();
                 }
                 return _bookBuilder();

 <#elseif methodName == "customer">
   ${cd4c.method(" public static ASTCustomerBuilder customerBuilder ()")}
                    if (millASTCustomerBuilder == null) {
                      millASTCustomerBuilder = getMill();
                    }
                    return _customerBuilder();

 <#elseif methodName == "inventoryManager">
   ${cd4c.method(" public static ASTInventoryManagerBuilder inventoryManagerBuilder ()")}
                       if (millASTInventoryManagerBuilder == null) {
                         millASTInventoryManagerBuilder = getMill();
                       }
                       return _inventoryManagerBuilder();

   <#elseif methodName == "paymentGateway">
      ${cd4c.method(" public static ASTPaymentGatewayBuilder paymentGatewayBuilder ()")}
                            if (millASTPaymentGatewayBuilder == null) {
                              millASTPaymentGatewayBuilder = getMill();
                            }
                            return _paymentGatewayBuilder();

      <#elseif methodName == "store">
         ${cd4c.method(" public static ASTStoreBuilder storeBuilder ()")}
                                     if (millASTStoreBuilder == null) {
                                       millASTStoreBuilder = getMill();
                                     }
                                     return _storeBuilder();

 </#if>
