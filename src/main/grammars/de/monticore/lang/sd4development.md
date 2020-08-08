<!-- (c) https://github.com/MontiCore/monticore -->
<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# Sequence Diagrams (UML/P SD)
The module described in this document defines a MontiCore language for 
UML/P SDs. UML/P SDs are an SD variant that is suited e.g. for the 
modeling of tests and (in combination e.g. with OCL) 
for desired and unwanted behavior interactions. 
UML/P SDs are defined in [Rum16], [Rum17].
                       
    see also: http://mbse.se-rwth.de/

This module contains 
* two grammars, 
* context conditions, 
* a symbol table infrastructure including functionality for 
  creating symbol tables and (de-)serializing symbol tables, and
* Pretty-printers. 

In the next section, this document presents an [example model](#example-model). 
Afterwards, the [main class](#main-class-sd4developmenttool) that can be used for processing SD models 
is described. Then, the constituents of the [SD grammars](#grammars) are introduced.
The following section lists the [context conditions](#context-conditions) of the SD language.
Finally, the last section highlights the [symbol table infrastructure](#symbol-table) for the 
SD language.

## Example Model

<img width="800" src="../../../../../../doc/pics/SDOverviewExample.png" alt="The graphical syntax of an example SD" style="float: left; margin-right: 10px;">
<br><b>Figure 1:</b> The graphical syntax of an example SD.

&nbsp;  

Figure 1 depicts the SD ```bid``` in graphical syntax. In textual syntax, 
the SD is defined as follows:

``` 
sequencediagram Bid {

  kupfer912:Auction;
  bidPol:BiddingPolicy;
  timePol:TimingPolicy;
  theo:Person;

  kupfer912 -> bidPol : validateBid(bid) {
    bidPol -> kupfer912 : return BiddingPolicy.OK;
  }
  kupfer912 -> timePol : newCurrentClosingTime(kupfer912,bid) {
    timePol -> kupfer912 : return t;
  }
  assert t.timeSec == bid.time.timeSec + extensionTime;
  let int m = theo.messages.size;
  kupfer912 -> theo : sendMessage(bm) {
    theo -> kupfer912 : return;
  }
  assert m + 1 == theo.messages.size;
}
```

## Main Class ```SD4DevelopmentTool```

The class [```SD4DevelopmentTool```](../../../../java/de/monticore/lang/sd4development/SD4DevelopmentTool.java) provides typical functionality used when
processing models. To this effect, the class provides methods
for parsing, pretty-printing, creating symbol tables, storing symbols, and 
loading symbols. Detailed information about the methods can be found in the Javadoc documentation
of the class [```SD4DevelopmentTool```](../../../../java/de/monticore/lang/sd4development/SD4DevelopmentTool.java). 

## Grammars
This module contains the two grammars [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) 
and [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4). 

### SDBasis
The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) contains the basic constituents to define textual 
representations of UML/P SDs. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../../grammars/de/monticore/lang/SDBasis.mc4). 

The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) defines the syntax for 
* SD artifacts, 
* SDs, 
* SD match modifiers, 
* objects, and 
* interactions for sending messages.   
                            
The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) extends the grammars
* [MCBasicTypes](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4) for adding the possibility to define objects typed as 
  MCObjectTypes.
* [TypeSymbols](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/TypeSymbols.mc4) to be able to use symbols of kind Variable and to enable 
  typechecking. Objects and local variables are added as variable symbols 
  to the symbol table.
* [ExpressionsBasis](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4) to be able to reuse visitors for typechecking.
* [UMLStereotype](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLStereotype.mc4) for adding UMLStereotypes as possible extension points to the grammar. 

### SD4Development
The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) extends the grammar SDBasis with advanced concepts 
used in object-oriented programming and embends OCL for defining local 
variables and stating conditions. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../../grammars/de/monticore/lang/SD4Development.mc4). 

The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) defines the syntax for
* actions representing method calls,
* actions representing constructor calls,
* actions representing returns,
* actions representing the throw of an exception,
* interactions representing the end of method calls,
* classes as targets of interactions to model static method calls,
* conditions, and
* local variable declarations.

The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) extends the grammars
* [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) to reuse the basic constituents of SDs,
* [MCCommonLiterals](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4) to be able to use common literals in expressions.
* [CommonExpressions](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4) to be able to use simple expressions, e.g. a == b.
* [OCLExpressions](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/OCLExpressions.mc4) for embedding OCL expressions as conditions and to be 
  able to use OCL expressions for the definition of local variables.

## Context Conditions
This section lists the context conditions for the [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) grammar and the 
context conditions for the [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) grammar.

### SDBasis Context Conditions
The implementations of the context conditions for the [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) grammar are 
located [here](../../../../java/de/monticore/lang/sdbasis/_cocos).

* The context condition [```CommonFileExtensionCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/CommonFileExtensionCoco.java)  checks if an artifact
containing an SD model has the common file ending ".sd" of SD artifacts.

* The context condition [```ObjectNameNamingConventionCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/ObjectNameNamingConventionCoco.java) checks the 
naming convention for objects in an SD, i.e., every object
name is not empty and starts with a lower case letter.

* The context condition [```PackageNameIsFolderNameCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/PackageNameIsFolderNameCoco.java) checks if the 
package names of SD artifacts corresponds to their actual 
locations in the file system.

* The context condition [```ReferencedObjectSourceDeclaredCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/ReferencedObjectSourceDeclaredCoco.java) checks 
if used source objects are declared before they are used.

* The context condition [```ReferencedObjectTargetDeclaredCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/ReferencedObjectTargetDeclaredCoco.java) checks 
if used target objects are declared before they are used.

* The context condition [```ReferencedTypeExistsCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/ReferencedTypeExistsCoco.java) checks if used 
types are defined.

* The context condition [```SDNameIsArtifactNameCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/SDNameIsArtifactNameCoco.java) checks if the 
names of SDs are equal to the file names of the artifacts
containing the SDs.

* The context condition [```SendMessageHasSourceOrTargetCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/SendMessageHasSourceOrTargetCoco.java) checks 
if SDSendMessage interactions have a source or a target.

* The context condition [```TypeNamingConventionCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/TypeNamingConventionCoco.java) checks if the 
naming convention for types is satisfied, i.e. that type names start 
with upper case letters.

* The context condition [```UniqueObjectNamingCoco```](../../../../java/de/monticore/lang/sdbasis/_cocos/UniqueObjectNamingCoco.java) checks if every 
object defined in an SD has a unique name.

### SD4Development Context Conditions 
The implementations of the context conditions for the [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) grammar 
are located [here](../../../../java/de/monticore/lang/sd4development/_cocos).

* The context condition [```ConstructorObjectNameNamingConventionCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/ConstructorObjectNameNamingConventionCoco.java) 
checks if naming conventions for objects constructed with SDNew 
interactions are satisfied, i.e., the name is not empty and starts
with a lower case letter.

* The context condition [```CorrectObjectConstructionTypesCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/CorrectObjectConstructionTypesCoco.java) checks
if the type declared for an object instantiated with an SDNew interaction 
and the object's initialization type are compatible.

* The context condition [```EndCallHasSourceOrTargetCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/EndCallHasSourceOrTargetCoco.java) checks if 
EndCall interactions have a source or a target.

* The context condition [```MethodActionRefersToCorrectTargetCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/MethodActionRefersToCorrectTargetCoco.java) 
checks if a method action refers to a correct target, i.e., that 
static methods are only called on classes and that all other methods 
are called on objects.

* The context condition [```MethodActionValidCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/MethodActionValidCoco.java) checks if a 
method action is valid, i.e., whether a method with a corresponding 
signature is defined for the type of the target of the interaction. 
The name of the method as well as the number and types of the method 
parameters must be equal.

* The context condition [```ReturnOnlyAfterMethodCoco```](../../../../java/de/monticore/lang/sd4development/_cocos/ReturnOnlyAfterMethodCoco.java) checks if 
return actions only appear after corresponding method calls.

## Symbol Table
The SD language introduces the [```SequenceDiagramSymbol```](../../../../grammars/de/monticore/lang/SDBasis.mc4)
symbol type. Additionally, the SD language uses the build-in
symbol type [```VariableSymbol```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) 
and [type symbol types](https://github.com/MontiCore/monticore/tree/dev/monticore-grammar/src/main/grammars/de/monticore/types) of MontiCore. 

Each SD may define objects. Therefore, SDs may 
export [```VariableSymbols```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) 
containing the information about the name and the type of the object. Possible types for objects are 
[```MCObjectTypes```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4). Further, it is possible to dynamically instantiated 
variables via [```SDVariableDeclarations```](../../../../grammars/de/monticore/lang/SD4Development.mc4) by using [OCL expressions](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/OCLExpressions.mc4). 
Interactions may call methods of objects.
Therefore, for checking whether the types of objects and variables
are defined and for checking whether methods used in interactions are 
defined, SDs may import [```TypeSymbols```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) 
and [```OOTypeSymbols```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/TypeSymbols.mc4). 

### Symbol Table Data Structure

<img width="400" src="../../../../../../doc/pics/STDataStructure.png" alt="The data structure of the symbol table of the SD language" style="float: left; margin-right: 10px;">
<br><b>Figure 2:</b> The data structure of the symbol table of the SD language.

&nbsp;  

Figure 2 depicts the symbol table data structure of the [```SD4Development```](../../../../grammars/de/monticore/lang/SD4Development.mc4)
grammar. The ```SD4DevelopmentGlobalScope``` is associated to an
```SD4DevelopmentArtifactScope``` for each artifact defining an SD. In each
of these artifacts, at most one SD can be defined and each SD introduces 
an [```SequenceDiagramSymbol```](../../../../grammars/de/monticore/lang/SDBasis.mc4). Therefore, each 
```SD4DevelopmentArtifactScope``` is associated to exactly one 
[```SequenceDiagramSymbol```](../../../../grammars/de/monticore/lang/SDBasis.mc4). 
Each [```SequenceDiagramSymbol```](../../../../grammars/de/monticore/lang/SDBasis.mc4) spans an 
```SD4DevelopmentScope```. This scope contains exactly one subscope, which 
is spanned by [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4). 
The scope associated to the [```SequenceDiagramSymbol```](../../../../grammars/de/monticore/lang/SDBasis.mc4)
contains a [```VariableSymbol```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) for each object that is defined inside the 
SD. The scope spanned by [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4) contains a 
[```VariableSymbol```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) for 
each object that is dynamically instantiated via [```SDNew```](../../../../grammars/de/monticore/lang/SD4Development.mc4) and for each 
variable that is instantiated via [```SDVariableDeclaration```](../../../../grammars/de/monticore/lang/SD4Development.mc4).  

<img width="500" src="../../../../../../doc/pics/SDSymtabExample.png" alt="The SD defines two objects and dynamically instantiated an object as well as a variable." style="float: left; margin-right: 10px;">
<br><b>Figure 3:</b> The SD defines two objects and dynamically instantiated an object as well as a variable.

&nbsp;  

Figure 3 depicts the simple example SD ```Size```. The SD defines 
the two objects ```kupfer912:Auction``` and ```theo:Person```. Additionally,
it dynamically instantiated the object ```bm:BidMessage``` and the variable
```int m```. The following depicts the textual syntax of the SD:

```
sequencediagram Size {

  kupfer912:Auction;
  theo:Person;

  kupfer912 -> BidMessage bm = new BidMessage(...);
  let int m = theo.messages.size;
  kupfer912 -> theo : sendMessage(bm);
  theo -> kupfer912 : return;
  assert m + 1 == theo.messages.size;
}
```  

Figure 4 depicts the symbol table instance for the SD ```Size```. 
Figure 4 abstracts from the ```SD4DevelopmentGlobalScope``` and
 ```SD4DevelopmentArtifactScope``` instances. The two objects 
 ```kupfer912:Auction``` and ```theo:Person``` correspond to the 
 [```VariableSymbol```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4)
 instances linked to the ```SD4DevelopmentScope``` with the ```name``` attribute
 containing the value ```"Size"```. The dynamically instantiated object 
 ```bm:BidMessage``` and the variable ```int m``` correspond to the 
 [```VariableSymbols```](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/BasicTypeSymbols.mc4) of the other ```SD4DevelopmentScope```, which is introduced by
 [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4).    

<img width="500" src="../../../../../../doc/pics/STInstanceExample.png" alt="Symbol table instance of the SD depicted in Figure 3" style="float: left; margin-right: 10px;">
<br><b>Figure 4:</b> Symbol table instance of the SD depicted in Figure 3.

&nbsp;  

The handwritten extensions of the symbol table creator of the 
[```SDBasis```](../../../../grammars/de/monticore/lang/SDBasis.mc4)
grammar can be found in the class 
[```SDBasisSymbolTableCreator```](../../../../java/de/monticore/lang/sdbasis/_symboltable/SDBasisSymbolTableCreator.java). The handwritten
extensions of the symbol table creator of the [```SD4Development```](../../../../grammars/de/monticore/lang/SD4Development.mc4) grammar 
can be found in the class [```SD4DevelopmentSymbolTableCreator```](../../../../java/de/monticore/lang/sd4development/_symboltable/SD4DevelopmentSymbolTableCreator.java). 

### Serialization and De-serialization of Symbol Tables

The SD language uses the DeSer implementations as generated by MontiCore
without any handwritten extensions. The objects that are dynamically instantiated
with interactions via [```SDNew```](../../../../grammars/de/monticore/lang/SD4Development.mc4) interactions and the local variables 
introduced via [```SDVariableDeclaration```](../../../../grammars/de/monticore/lang/SD4Development.mc4)  by using statements are not exported 
(they are defined in an unnamed inner scope introduced with [```SCBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4)).
Therefore, the corresponding variable symbols are not serialized, i.e., they
are not part of the corresponding symbol file. For example, the following 
depicts the symbol file obtained from serializing the symbol table instance 
depicted in Figure 4:

```json
{
  "kind": "de.monticore.lang.sd4development._symboltable.SD4DevelopmentArtifactScope",
  "name": "size",
  "package": "examples.correct",
  "sequenceDiagramSymbols": [
    {
      "kind": "de.monticore.lang.sdbasis._symboltable.SequenceDiagramSymbol",
      "name": "examples.correct.size",
      "spannedScope": {
        "kind": "de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope",
        "name": "size",
        "isShadowingScope": false,
        "variableSymbols": [
          {
            "kind": "de.monticore.types.basictypesymbols._symboltable.VariableSymbol",
            "name": "examples.correct.size.kupfer912",
            "type": {
              "kind": "de.monticore.types.check.SymTypeOfObject",
              "objName": "Auction"
            },
            "isReadOnly": false
          },
          {
            "kind": "de.monticore.types.basictypesymbols._symboltable.VariableSymbol",
            "name": "examples.correct.size.theo",
            "type": {
              "kind": "de.monticore.types.check.SymTypeOfObject",
              "objName": "Person"
            },
            "isReadOnly": false
          }
        ]
      }
    }
  ]
}
```

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md).
* [MontiCore documentation](http://www.monticore.de/)
* [Publications about MBSE](https://www.se-rwth.de/publications/)

