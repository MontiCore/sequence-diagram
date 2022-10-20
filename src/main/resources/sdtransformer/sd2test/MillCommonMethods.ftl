<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "initMe">
${cd4c.method(" public static void initMe (BookstoreMill a)")}
    mill = a;
    millASTBookBuilder = a;
    millASTCustomerBuilder = a;
    millASTInventoryManagerBuilder = a;
    millASTPaymentGatewayBuilder = a;
    millASTStoreBuilder = a;

<#elseif methodName == "reset">
${cd4c.method(" public static void reset ()")}

    mill = null;
    millASTBookBuilder = null;
    millASTCustomerBuilder = null;
    millASTInventoryManagerBuilder = null;
    millASTPaymentGatewayBuilder = null;
    millASTStoreBuilder = null;

<#elseif methodName == "getMill">
${cd4c.method(" protected static BookstoreMill getMill ()")}

  if (mill == null) {
    mill = new BookstoreMill();
  }
  return mill;

<#elseif methodName == "init">
${cd4c.method(" public static void init ()")}

    mill = new BookstoreMill();
</#if>
