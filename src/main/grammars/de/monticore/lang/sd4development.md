<!-- (c) https://github.com/MontiCore/monticore -->
<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# Sequence Diagrams (UML/P SD)
The module descriped in this document defines a MontiCore language for 
UML/P SDs. UML/P SDs are a SD variant that is especially suited for the 
modeling of tests. UML/P SDs are defined in detail in 
the following books:
                      
    [Rum16] B. Rumpe: Modeling with UML:
    Language, Concepts, Methods. Springer International, July 2016.
                       
    [Rum17] B. Rumpe: Agile Modeling with UML:
    Code Generation, Testing, Refactoring. Springer International, May 2017.
                       
    see also: http://mbse.se-rwth.de/

This module contains 
* two grammars, 
* context conditions, 
* a symbol table infrastructure including functionality for 
  creating symbol tables and (de)-serializing symbol tables, and
* Pretty Printers. 

The following outline overviews the sections of this document.

## Grammars
This module contains the two grammars SDBasis and SD4Development. 

### SDBasis
The grammar SDBasis contains the basic constituents to define textual 
representations of UML/P SDs. A detailed documentation of the grammar can 
be found in the artifact defining the grammar. 

The grammar SDBasis defines the syntax for 
* SD artifacts, 
* SDs, 
* SD match modifiers, 
* objects, and 
* interactions for sending messages.   
                            
The grammar SDBasis extends the grammars
* MCBasicTypes for adding the possibility to define objects typed as 
  MCObjectTypes.
* TypeSymbols to be able to use symbols of kind Variable and to enable 
  typechecking. Objects and local variables are added as variable symbols 
  to the symbol table.
* ExpressionsBasis to be able to reuse visitors for typechecking.
* UMLStereotype for adding UMLStereotypes as possible extension points to the grammar. 

### SD4Development
The grammar SD4Development extends the grammar SDBasis with advanced concepts 
used in object-oriented programming and embends OCL for defining local 
variables and stating conditions. A detailed documentation of the grammar can 
be found in the artifact defining the grammar. 

The grammar SD4Development defines the syntax for
* actions representing method calls,
* actions representing constructor calls,
* actions representing returns,
* actions representing the throw of an exception,
* interactions representing the end of method calls,
* classes as targets of interactions to model static method calls,
* ocl conditions, and
* local variable declarations.

The grammar SD4Development extends the grammars
* SDBasis to reuse the basic constituents of SDs,
* MCCommonLiterals to be able to common literals in expressions.
* CommonExpressions to be able to use simple expressions, e.g. a == b
* OCLExpressions for embedding OCL expressions as conditions and to be 
  able to use expressions for the definition of local variables.

## Context Conditions
This section lists the context conditions for the SDBasis grammar and the 
context conditions for the SD4Development grammar.

### SDBasis Context Conditions
The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

### SD4Development Context Conditions 
The context condition ```ConstructorObjectNameNamingConventionCoco``` checks 
if naming conventions for objects constructed with SDNew constructs are 
satisfied, i.e., the name is not empty and starts with a lower case letter.

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

The context condition ```ConstructorObjectNameNamingConventionCoco```

 
### SDBasis

### SD4Development

## Symbol Table

### SDBasis

### SD4Development

