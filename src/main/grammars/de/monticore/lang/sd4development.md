<!-- (c) https://github.com/MontiCore/monticore -->
<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# Sequence Diagrams (UML/P SD) 
The module described in this document defines a MontiCore language for 
UML/P SDs. UML/P SDs are an SD variant that is suited e.g. for the 
modeling of tests and (in combination e.g. with OCL) 
for desired and unwanted behavior interactions. 
UML/P SDs are defined in [Rum16], [Rum17].
                       

This module contains 
* two grammars, 
* context conditions, 
* a symbol table infrastructure including functionality for 
  creating symbol tables and (de-)serializing symbol tables, and
* pretty-printers. 


## An Example Model

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

This was for us the most intuitive textual representation of SDs, which was not 
easy to define, because SDs are inherently two dimensional with their 
objects, activity bars and interactions in time.

## Command Line Interface (CLI) Usage

This section describes the CLI for the SD language. 

### Main Class ```SD4DevelopmentCLI```
The class [```SD4DevelopmentCLI```](../../../../java/de/monticore/lang/sd4development/SD4DevelopmentCLI.java) provides typical functionality used when
processing models. To this effect, the class provides methods
for parsing, pretty-printing, creating symbol tables, storing symbols, and 
loading symbols. The class provides a `main` method and can thus be used as CLI as described in the following.

### Building the CLI from Sources
It is possible to build an executable JAR of the CLI from the sources in GitHub.

For building an executable Jar of the CLI with Bash from the sources available in GitHub, execute the following commands.
Executing the commands generates the executable JAR file `target/libs/SD4DevelopmentCLI.jar`.
```
clone 
```

### Getting started

#### First steps
Executing the produced Jar file without any options prints a usage information of the CLI tool on the console:
```
java -jar SD4DevelopmentCLI.jar                        
usage: SD4DevelopmentCLI
 -c,--coco <arg>            Checks the CoCos for the input FDs. Possible
                            arguments are 'intra',  'inter', and 'type'. When
                            given the argument 'intra', only the intra-model
                            CoCos are checked. When given the argument 'inter',
                            only the intra- and inter-model CoCos are checked.
                            When given the argument 'type', all CoCos are
                            checked. When no argument is specified, all CoCos
                            are checked by default.
 -h,--help                  Prints this help dialog.
 -i,--input <arg>           Processes the SD input artifacts specified as
                            arguments. At least one input SD is mandatory.
 -mp,--modelpath <arg>      Sets the artifact paths for imported symbols.
 -pp,--prettyprint <file>   Prints the input SDs to stdout or to the specified
                            files (optional).
 -sd,--semdiff              Computes a diff witness contained in the semantic
                            difference from the first input SD to the second
                            input SD if one exists and prints it to stdout.
                            Requires exactly two  SDs as inputs. If no diff
                            witness exists, it prints that the first SD is a
                            refinement  of the second SD to stdout.
 -ss,--storesymbols <arg>   Stores the serialized symbol tables of the input SDs
                            in the specified files. The n-th input SD is stored
                            in the file as specified by the n-th argument. If no
                            arguments are given, the serialized symbol tables
                            are stored in
                            'target/symbols/{packageName}/{artifactName}.sdsym'
                            by default.
```
To work properly, the CLI tool needs the mandatory argument `-i,--input <arg>`, which takes the file paths of at least one input model.
The model has to conform to the sequence diagram language specification, which is given in the [Grammars](#grammars) section.
If no other arguments are specified, the CLI tool just parses the model(s), and then checks every context condition that is given in [this](#context-conditions) section.
You can try this by executing the following command
```
java -jar SD4DevelopmentCLI.jar -i example.sd
```
with the most trivial sequence diagram model:
```
sequencediagram example {
}
```
You may notice, that the CLI tool prints no output on the console.
This means, that the tool has processed the model successfully, and the model does not violate any of the context conditions.

#### Pretty printing
The CLI tool also provides a pretty printer for the sequence diagram language.
A pretty printer can be used, e.g., to fix the formatting of a sequence diagram model.
To execute the pretty printer, the `-pp,--prettyprint` option can be used:
```
java -jar SD4DevelopmentCLI.jar -pp -i example.sd
```
which just prints the (formatted) input model on the console:
```
sequencediagram example {
}
```

#### Checking Context Conditions
If you are only interested in checking a model on a subset of the context conditions, you can do this by restricting the type of included context conditions.
In particular, there are three different kinds of context conditions: 
* `intra` context conditions only concern violations withtin a single model.
* `inter` context conditions include, in addition to `intra` context conditions, checks which include multiple models referencing each other.
* `type` include all context coniditions, and in addition violations which concern the usage of types.

Consider the `Bid` sequence diagram from the [introduction](#an-example-model).
You can check the different kinds of context conditions, using the `-c,--coco <arg>` option:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c intra
```
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c inter
```
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type
```
After executing the last command, you may notice that CLI tool indeed produces some output:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type
... ERROR ROOT - Bid.sd:<3,2>: 0xB0028: Type 'Auction' is used but not defined.
```
The error message indicates that there is a problem in the third line, i.e., there seems to be a problem with this statement `kupfer912:Auction;`.
And indeed, the tool tries to load some type information about the `Auction` type.
However, we never defined this type at any place, and therefore the tool is not able to find any information of the `Auction` type.

#### Using the model path to resolve symbols
There are several options to resolve the previous error.
However, in this section we make use of the model path and provide the tool with a serialized version of another model, which contains the necessary type informations.


A serialized version of a model, which provides all necessary type information, can be found [here](../../../../../../doc/Types.cdsym).
The path in which the seralized model file is stored, is called the "model path".
If we provide the model path to the tool, it will search for symbols which are stored within the model path.
So, if we want to tool to find our serialized model, we have to provide the model path to the tool via the `-mp,--modelpath <arg>` option:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type -mp <MODELPATH>
```
where `<MODELPATH>` is the path where you stored the serialized model.

But executing the above command still produces the same error message.
This is because the model needs to be imported first, just like in Java.
So we add the following import statement to the top of the `Bid.sd` model:
```
import Types.*;

sequencediagram Bid {
  ...
}
```
which means that we want to import everything, which is stored in the `Types` model.
Note that you may have to change the name here, depending on how you named the serialized model from above.
If we now execute the command again, the CLI tool will print no output, and therefore processed the model successfully, without any context condition violations.
Great!

#### Storing symbols
In the previous section, we saw how to load symbols from an existing serialized model.
Now, we will use the CLI tool to serialize our `Bid.sd` model, just by executing the following command:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -ss -mp <MODELPATH>
```
The `-ss,--storesymbols <arg>` transforms the given models into a serialized version of it and stores the results in the file paths given as arguments.
Note that if you don't provide an argument here, the CLI tool will just serialize every model specified in the `-i,--input <arg>` option, and stores it in `target/symbols/{packageName}/{artifactName}.sdsym` in the working directory, i.e., the directory where you executed the above command.
Furthermore, please notice that in order to store the symbols properly, the model has to be well-formed in all regards, and therefore all context conditions (i.e., the `type` mode) are checked beforehand.

That said, the CLI tool produces the following file `target/symbols/Bid.sdsym` which can now be used by other models, e.g., which want to use some of the variables defined here:
```json
{
    "name": "Bid",
    "kindHierarchy": [
        [
            "de.monticore.lang.sd4development._symboltable.FieldSymbol",
            "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol"
        ],
        [
            "de.monticore.lang.sd4development._symboltable.TypeVarSymbol",
            "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
        ],
        [
            "de.monticore.lang.sd4development._symboltable.MethodSymbol",
            "de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol"
        ],
        [
            "de.monticore.lang.sd4development._symboltable.OOTypeSymbol",
            "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
        ]
    ],
    "symbols": [
        {
            "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
            "name": "kupfer912",
            "type": {
                "kind": "de.monticore.types.check.SymTypeOfObject",
                "objName": "Auction"
            },
            "isReadOnly": false
        },
        {
            "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
            "name": "bidPol",
            "type": {
                "kind": "de.monticore.types.check.SymTypeOfObject",
                "objName": "BiddingPolicy"
            },
            "isReadOnly": false
        },
        {
            "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
            "name": "timePol",
            "type": {
                "kind": "de.monticore.types.check.SymTypeOfObject",
                "objName": "TimingPolicy"
            },
            "isReadOnly": false
        },
        {
            "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
            "name": "theo",
            "type": {
                "kind": "de.monticore.types.check.SymTypeOfObject",
                "objName": "Person"
            },
            "isReadOnly": false
        },
        {
            "kind": "de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol",
            "name": "Bid"
        }
    ]
}
```

#### Semantic Differencing
For this section, consider the sequence diagrams [rob1.sd](../../../../../test/resources/sddiff/rob1.sd) and [rob2.sd](../../../../../test/resources/sddiff/rob2.sd).
Both are depicted the Figure 2.

<img width="800" src="../../../../../../doc/pics/rob1_2.png" alt="The graphical syntax of an example SD" style="float: left; margin-right: 10px;">
<br><b>Figure 2:</b> Two sequence diagrams chosen for semantic differencing.

&nbsp;  

Now we want to compute the semantic differences between these sequence diagrams, whereby the semantics of a sequence diagram is defined as the set of all possible system runs of a sequence diagram.
The semantic difference of two SDs $sd_1$ and $sd_2$ is therefore the set of all possible runs of $sd_1$ which are not possible in $sd_2$.
To calculate the semantic difference between two given SDs, the CLI tool provides the `-sd,--semdiff` option:
```
java -jar SD4DevelopmentCLI.jar -sd -i rob1.sd rob2.sd
```
And because [rob1.sd](../../../../../test/resources/sddiff/rob1.sd) is a refinement of  [rob2.sd](../../../../../test/resources/sddiff/rob2.sd), the tool prints the following output:
```
The input SD 'rob1.sd' is a refinement of the input SD 'rob2.sd'
```
However, because the semantic difference operator is by no means commutative, swapping the arguments changes the result:
```
java -jar SD4DevelopmentCLI.jar -sd -i rob2.sd rob1.sd
```
```
Diff witness:
ui -> controller : deliver(r4222,wd40),
controller -> planner : getDeliverPlan(r4222,wd40),
planner -> controller : plan,
planner -> stateProvider : getState(),
controller -> actionExecutor : moveTo(r4222),
actionExecutor -> controller : ACTION_SUCCEEDED
```

## Grammars

This module contains the two grammars [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) 
and [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4). 

### SDBasis
The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) contains the basic constituents to define textual 
representations of UML/P SDs. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../../grammars/de/monticore/lang/SDBasis.mc4). 

The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) defines the syntax for 
* SD artifact, 
* SD match modifiers (a UMLP extension [Rum16]), 
* objects, and 
* interactions for sending messages
* including (optional) activity bars.   
                            
The grammar [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) extends the grammars
* [MCBasicTypes][XXX] for adding the possibility to define objects typed as 
  MCObjectTypes, to be able to use symbols of kind Variable, and to enable typechecking. 
  Objects and local variables are added as variable symbols to the symbol table.
* [BasicSymbols][BasicSymbolsRef] for using symbols of kind Diagram und Variable.
* [ExpressionsBasis][ExpressionsBasisRef] to be able to reuse visitors for typechecking.
* [UMLStereotype][UMLStereotypeRef] for adding UMLStereotypes as possible extension points to the grammar. 

### SD4Development
The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) extends the grammar SDBasis with advanced concepts 
used in object-oriented programming and embeds OCL for defining local 
variables and stating conditions. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../../grammars/de/monticore/lang/SD4Development.mc4). 

The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) defines the syntax for
* method calls,
* constructor calls,
* returns,
* the throw of an exception,
* interactions representing the end of method calls,
* classes as targets of interactions to model static method calls,
* conditions, and
* local variable declarations.

The grammar [SD4Development](../../../../grammars/de/monticore/lang/SD4Development.mc4) extends the grammars
* [SDBasis](../../../../grammars/de/monticore/lang/SDBasis.mc4) to reuse the basic constituents of SDs,
* [MCCommonLiterals][MCCommonLiteralsRef] to be able to use common literals in expressions.
* [CommonExpressions][CommonExpressionsRef] to be able to use simple expressions, e.g. a == b.
* [OCLExpressions][OCLExpressionsRef] for embedding OCL expressions as conditions and to be 
  able to use OCL expressions for the definition of local variables.
* [OOSymbols][OOSymbolsRef] to be able to import OOType Symbols.

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

The SD language uses the build-in
symbol types [```VariableSymbol```][BasicSymbolsRef]
and [```DiagramSymbol```][BasicSymbolsRef]
as well as the [type symbol types][TypeSymbolsRef] of MontiCore. 

Each SD may define objects. Therefore, SDs may 
export [```VariableSymbols```][BasicSymbolsRef] 
containing the information about the name and the type of the object. Possible types for objects are 
[```MCObjectTypes```][MCBasicTypesRef]. Further, it is possible to dynamically instantiated 
variables via [```SDVariableDeclarations```](../../../../grammars/de/monticore/lang/SD4Development.mc4) by using [OCL expressions][OCLExpressionsRef]. 
Interactions may call methods of objects.
Therefore, for checking whether the types of objects and variables
are defined and for checking whether methods used in interactions are 
defined, SDs may import [```TypeSymbols```][BasicSymbolsRef] 
and [```OOTypeSymbols```][OOSymbolsRef]. 

### Symbol Table Data Structure

<img width="600" src="../../../../../../doc/pics/STDataStructure.png" alt="The data structure of the symbol table of the SD language" style="float: left; margin-right: 10px;">
<br><b>Figure 3:</b> The data structure of the symbol table of the SD language.

&nbsp;  

Figure 3 depicts the symbol table data structure of the [```SD4Development```](../../../../grammars/de/monticore/lang/SD4Development.mc4)
grammar. The ```SD4DevelopmentGlobalScope``` is associated to an
```SD4DevelopmentArtifactScope``` for each artifact defining an SD. In each
of these artifacts, at most one SD can be defined and each SD introduces 
a [```DiagramSymbol```][BasicSymbolsRef]. 
Therefore, each ```SD4DevelopmentArtifactScope``` is associated to exactly one 
[```DiagramSymbol```][BasicSymbolsRef]. 
The ```SD4DevelopmentArtifactScope``` scope contains exactly one subscope, which 
is spanned by [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4). 
The ```SD4DevelopmentArtifactScope```
contains a [```VariableSymbol```][BasicSymbolsRef] for each object that is defined inside the 
SD. The scope spanned by [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4) contains a 
[```VariableSymbol```][BasicSymbolsRef] for 
each object that is dynamically instantiated via [```SDNew```](../../../../grammars/de/monticore/lang/SD4Development.mc4) and for each 
variable that is instantiated via [```SDVariableDeclaration```](../../../../grammars/de/monticore/lang/SD4Development.mc4).  

<img width="600" src="../../../../../../doc/pics/SDSymtabExample.png" alt="The SD defines two objects and dynamically instantiated an object as well as a variable." style="float: left; margin-right: 10px;">
<br><b>Figure 4:</b> The SD defines two objects and dynamically instantiated an object as well as a variable.

&nbsp;  

Figure 4 depicts the simple example SD ```Size```. The SD defines 
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

Figure 5 depicts the symbol table instance for the SD ```Size```. 
Figure 5 abstracts from the ```SD4DevelopmentGlobalScope``` instance. 
The two objects ```kupfer912:Auction``` and ```theo:Person``` correspond to the 
 [```VariableSymbol```][BasicSymbolsRef]
 instances linked to the ```SD4DevelopmentArtifactScope```. 
 The object ```:DiagramSymbol``` is linked to the ```SD4DevelopmentArtifactScope``` and has the 
 same name as the SD defining the symbol.
 The dynamically instantiated object 
 ```bm:BidMessage``` and the variable ```int m``` correspond to the 
 [```VariableSymbols```][BasicSymbolsRef] of the ```SD4DevelopmentScope```, which is introduced by
 [```SDBody```](../../../../grammars/de/monticore/lang/SDBasis.mc4).    

<img width="600" src="../../../../../../doc/pics/STInstanceExample.png" alt="Symbol table instance of the SD depicted in Figure 3" style="float: left; margin-right: 10px;">
<br><b>Figure 5:</b> Symbol table instance of the SD depicted in Figure 3.

&nbsp;  

The handwritten extensions of the symbol table creator of the 
[```SDBasis```](../../../../grammars/de/monticore/lang/SDBasis.mc4)
grammar can be found in the class 
[```SDBasisSymbolTableCreator```](../../../../java/de/monticore/lang/sdbasis/_symboltable/SDBasisSymbolTableCreator.java). The handwritten
extensions of the symbol table creator of the [```SD4Development```](../../../../grammars/de/monticore/lang/SD4Development.mc4) grammar 
can be found in the class [```SD4DevelopmentSymbolTableCreator```](../../../../java/de/monticore/lang/sd4development/_symboltable/SD4DevelopmentSymbolTableCreator.java). 
Instances of class [```SD4DevelopmentSymbolTableCompleter```](../../../../java/de/monticore/lang/sd4development/_symboltable/SD4DevelopmentSymbolTableCompleter.java)
are responsible for calculating the type attributes of variable symbols and, thereby, for checking
whether used types are defined.

## Symbol kinds used by the SD language (importable or subclassed):
The SD language uses symbols of kind [```TypeSymbol```][BasicSymbolsRef],
[```VariableSymbol```][BasicSymbolsRef], and
[```DiagramSymbol```][BasicSymbolsRef] as well as 
the symbols that are used by these symbols (e.g. [```FunctionSymbol```][BasicSymbolsRef] 
is used by [```TypeSymbol```][BasicSymbolsRef]).

## Symbol kinds defined by the SD language (exported):
None.      

## Symbols imported by SD models:
* SDs import [```VariableSymbols```][BasicSymbolsRef]. 
The objects represented by imported [```VariableSymbols```][BasicSymbolsRef]
can be used as sources or targets of interactions.
* SDs import [```TypeSymbols```][BasicSymbolsRef]. The imported types can be used as types for objects and 
variables introduced via inline declarations. 

## Symbols exported by SD models:
* SD models export [```VariableSymbols```][BasicSymbolsRef]. 
For each object defined in an SD, the SD exports a corresponding [```VariableSymbol```][BasicSymbolsRef]. 
* Each SD exports exactly one [```DiagramSymbol```][BasicSymbolsRef] corresponding to the SDArtifact.

## Serialization and De-serialization of Symbol Tables

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
  "name": "Bid",
  "package": "examples.correct",
  "kindHierarchy": [
    [
      "de.monticore.lang.sd4development._symboltable.FieldSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol"
    ],
    [
      "de.monticore.lang.sd4development._symboltable.TypeVarSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
    ],
    [
      "de.monticore.lang.sd4development._symboltable.MethodSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol"
    ],
    [
      "de.monticore.lang.sd4development._symboltable.OOTypeSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
    ]
  ],
  "symbols": [
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
      "name": "kupfer912",
      "type": {
        "kind": "de.monticore.types.check.SymTypeOfObject",
        "objName": "Auction"
      },
      "isReadOnly": false
    },
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
      "name": "theo",
      "type": {
        "kind": "de.monticore.types.check.SymTypeOfObject",
        "objName": "Person"
      },
      "isReadOnly": false
    },
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol",
      "name": "bid"
    }
  ]
}
```

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

[BasicSymbolsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/BasicSymbols.mc4
[TypeSymbolsRef]:https://github.com/MontiCore/monticore/tree/dev/monticore-grammar/src/main/grammars/de/monticore/types
[MCBasicTypesRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4
[OOSymbolsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/OOSymbols.mc4
[ExpressionsBasisRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4
[UMLStereotypeRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLStereotype.mc4
[MCCommonLiteralsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4
[CommonExpressionsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4
[OCLExpressionsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/OCLExpressions.mc4
