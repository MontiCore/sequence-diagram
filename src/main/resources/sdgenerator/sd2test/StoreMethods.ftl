<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "checkAvailability">
${cd4c.method(" public  boolean checkAvailability (String bookId)")}
               return inventoryManager.checkQuantity(bookId) > 0;

 <#elseif methodName == "makePurchase">
   ${cd4c.method("public  String makePurchase(String bookId,String customerId)")}
 BookstoreDAO dao = new BookstoreDAO();


if (inventoryManager.checkQuantity(bookId) < 1) {
  return "book unavailable";
}
int amount = 0;
for(ASTBook b : dao.getBookList()){
    if(b.getBookId().equals(bookId)){
        amount = b.getPrice();
        break;
    }
}

 String paymentStatus = paymentGateway.processPayment(customerId,amount);
if (paymentStatus.equals("payment unsuccessful")) {
  return "payment unsuccessful";
}
//remove book from inventory
inventoryManager.removeBookFromInventory(bookId);
return "purchase successful";

 <#elseif methodName == "placeReturn">
   ${cd4c.method("public  String placeReturn (String bookId,String customerId)")}
   inventoryManager.addBookToInventory(bookId);
   //refund process
   return paymentGateway.processReturn(customerId);
 </#if>
