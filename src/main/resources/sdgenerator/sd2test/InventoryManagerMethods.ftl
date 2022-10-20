<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName")}
<#if methodName == "checkQuantity">
${cd4c.method("public int checkQuantity (String bookId)")}
              for (ASTBook book: books){
          if (book.getBookId().equals(bookId)) {
            return book.getQuantity();
          }
        }
        return 0;
 <#elseif methodName == "addInventory">
   ${cd4c.method("public void addBookToInventory (String bookId)")}
 BookstoreDAO dao = new BookstoreDAO();

for (ASTBook b: books){
  if (b.getBookId().equals(bookId)) {
    b.setQuantity(b.getQuantity() + 1);
    System.out.println("Book" + bookId + " quantity is " + b.getQuantity());
    return ;
  }
}
for(ASTBook b : dao.getBookList()){
    if(b.getBookId().equals(bookId)){
        books.add(b);
        break;
    }
 }
System.out.println("Book added: " + bookId);
System.out.println("Book " + bookId + " quantity is 1");

 <#elseif methodName == "removeInventory">
   ${cd4c.method("public void removeBookFromInventory (String bookId)")}
   for (ASTBook b: books){
        if (b.getBookId().equals(bookId)) {
          b.setQuantity(b.getQuantity() - 1);
          if (b.getQuantity() == 0) {
            books.remove(b);
          }
          System.out.println("Book " + bookId + " quantity is " + b.getQuantity());
          return ;
        }
      }
 </#if>
