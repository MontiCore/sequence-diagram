<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "processPayment">
${cd4c.method("public String processPayment(String customerId,int price)")}
               BookstoreDAO dao = new BookstoreDAO();

               for(ASTCustomer customer: dao.getCustomerList()){
                    if(customer.customerID.equals(customerId)){
                        if (customer.getAccountBalance() >= price) {
                            return "payment successful";
                        }
                    }
               }

               return "payment unsuccessful";
 <#elseif methodName == "processReturn">
   ${cd4c.method("public  String processReturn(String customerId)")}
  BookstoreDAO dao = new BookstoreDAO();

      for(ASTCustomer customer: dao.getCustomerList()){
          if(customer.customerID.equals(customerId)){
              if (customer.getAccountBalance() > -1) {
                  return "return successful";
              }
          }
      }


 return "return unsuccessful";

 </#if>
