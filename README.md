<!-- (c) https://github.com/MontiCore/monticore -->
<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

This documentation is intended for  **modelers** who use the sequence diagram (SD) languages.
A detailed documentation for **language engineers** using or extending the SD language is 
located **[here](src/main/grammars/de/monticore/lang/sd4development.md)**.
We recommend that **language engineers** read this documentation before reading the detailed
documentation.

# An Example Model

<img width="800" src="doc/pics/SDOverviewExample.png" alt="The graphical syntax of an example SD" style="float: left; margin-right: 10px;">
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

# Command Line Interface (CLI)

This section describes the CLI tool of the SD language. 
The CLI tool provides typical functionality used when
processing models. To this effect, it provides funcionality
for 
* parsing, 
* coco-checking, 
* pretty-printing, 
* creating symbol tables, 
* storing symbols in symbol files, 
* loading symbols from symbol files, and 
* semantic differencing. 

The requirements for building and using the SD CLI tool are that Java 8, Git, and Gradle are 
installed and available for use in Bash. 

The following describes how to build the CLI tool from the source files.
Afterwards, this document contains a tutorial for using the CLI tool.  

## Building the CLI from Sources
 
It is possible to build an executable JAR of the CLI tool from the source files located in GitHub.
The following describes the process for building the CLI tool from the source files using Bash.

For building an executable Jar of the CLI with Bash from the source files available
in GitHub, execute the following commands.

First, clone the repository:
```
git clone https://github.com/MontiCore/sequence-diagram.git
```
Change the directory to the root directory of the cloned sources:
```
cd sequence-diagram
```
Afterwards, build the source files with gradle (if `./gradlew.bat` is not recognized as a command, then use `./gradlew`):
```
./gradlew.bat build
```
Congratulations, you can find the executable JAR file `SD4DevelopmentCLI.jar` in
 the directory `target/libs` (accessible via `cd target/libs`).

## Tutorial: Getting started Using the SD CLI
The previous section describes how to produce an executable jar file
(SD CLI tool) from the source files located in GitHub. This section provides a tutorial for
using the produced SD CLI.   

### First steps
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
To work properly, the CLI tool needs the mandatory argument `-i,--input <arg>`, which takes the file paths of at least one input file containing SD models.
If no other arguments are specified, the CLI tool just parses the model(s), and then checks every context condition that is given in [this](#context-conditions) section.

For trying this out, create a text file containing the following simple SD:
```
sequencediagram example {
}
```

Save the text file as `example.sd` (in the directory where `SD4DevelopmentCLI.jar` is located). 

Now execute the following command:
```
java -jar SD4DevelopmentCLI.jar -i example.sd
```

You may notice that the CLI tool prints no output to the console.
This means that the tool has parsed the file `example.sd` successfully.

### Pretty printing
The CLI tool also provides a pretty printer for the SD language.
A pretty printer can be used, e.g., to fix the formatting of a sequence diagram model.
To execute the pretty printer, the `-pp,--prettyprint` option can be used.
Using the option without arguments pretty prints the models contained in the input files to the console.

Execute the following command for trying this out:
```
java -jar SD4DevelopmentCLI.jar -pp -i example.sd
```
The command prints the pretty printed model contained in the input file to the console:
```
sequencediagram example {
}
```

It is possible to pretty print the models contained in the input files to output files.
For this task, it is possible to provide the names of output files as arguments to the `-pp,--prettyprint` option.
If arguments for output files are provided, then the number of output files must be equal to the number of input files.
The i-th input file is pretty printed into the i-th output file.

Execute the following command for trying this out:
```
java -jar SD4DevelopmentCLI.jar -pp ppExample.sd -i example.sd
```
The command prints the pretty printed model contained in the input file into the file `ppExample.sd`.

### Checking Context Conditions
For checking context conditions, the `-c,--coco <arg>` option can be used. 
Using the options without any arguments checks whether the model satisfies all context conditions. 

If you are only interested in checking whether a model only satisfies a subset of the context conditions or want to explicate that all context conditions shoud 
be checked, you can do this by additionally providing one of the three
 arguments `intra`, `inter`, and `type`.
* Using the argument `intra` only executes context conditions concerning violations of intra-model context conditions.
  These context conditions, for example, check naming conventions. 
* Using the argument `inter` executes all intra-model context conditions and additionally checks whether imported `Variables`, i.e., objects are defined.
* Using the argument `type` executes all context coniditions. These context conditions include checking whether
  used types and methods exist. The behavior when using the argument `type` is the equal to the default behavior when using no arguments. 

Execute the following command for trying out a simple example:
```
java -jar SD4DevelopmentCLI.jar -i example.sd -c
```
You may notice that the CLI prints nothing to the console when executing this command.
This means that the model satisfies all context condtions. 

Let us now consider a more complicated example.
Recall the SD `Bid` from the `An Example Model` section above.
For continuing, copy the textual repreentation of the SD `Bid` and save it in a 
file `Bid.sd` in the directory where `SD4DevelopmentCLI.jar` is located.

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
After executing the last command, you may notice that the CLI tool produces some output:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type
... ERROR ROOT - Bid.sd:<3,2>: 0xB0028: Type 'Auction' is used but not defined.
```
The error message indicates that there is a problem in the third line, i.e., there seems to be a problem with this statement `kupfer912:Auction;`.
And indeed, the tool tries to load some type information about the `Auction` type.
However, we never defined this type at any place, and therefore the tool is not able to find any information of the `Auction` type.

There must be another model defining the type. 
The model must provide the information about the definition of this type to its 
environment via storing this information in its symbol file (its symbol table stored in the field system).
The symbol file of this model has to be imported in the SD model for accessing the type.
For the SD language, we have not fixed a language for defining types.
Instead, the types can be defined in arbitrary models of arbitrary languages, as long as
the information about the definitions of the types are stored in the symbol files of the models
and the SD imports these symbol files. 
This may sound complicated at this point, but conceptually it is actually quite simple. 
The following subsection presents how to fix the error in the example model `Bid.sd` 
by importing a symbol file defining the (yet undefined) types. 

### Using the model path to resolve symbols

In this section we make use of the model path and provide the CLI tool with
a symbol file (stored symbol table) of another model, which contains the necessary type informations.

Create a new directly `mytypes` in the directory where the CLI tool `SD4DevelopmentCLI.jar` is located.
The symbol file `Types.typesym` of a model, which provides all necessary type information, can be found [here](doc/Types.typesym).
Download the file, name it `Types.typesym`, and move it into the directory `mytypes`.

The contents of the symbol file are of minor importance for you as a language user. 
In case you are curious and had a look into the symbol file: 
The symbol file contains a JSON representation of the symbols defined in another model.
In this case, the symbol file contains information about defined types. 
Usually, the CLI tools of MontiCore languages automatically generate these files and
you, as a language user, must not be concerned with its contents. 
  
The path that contains the directory structure that contains the model file is called the "Model Path".
If we provide the model path to the tool, it will search for symbols in symbol files, which are stored in directories contained in the model path.
So, if we want the tool to find our symbol file, we have to provide the model path to the tool via the `-mp,--modelpath <arg>` option:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type -mp <MODELPATH>
```
where `<MODELPATH>` is the path where you stored the serialized model.
In our example, in case you stored the model in the directory `mytypes`,
execute the following command:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -c type -mp mytypes
```

However, executing the above command still produces the same error message.
This is because the symbol file needs to be imported first, just like in Java.
Therefore, we add the following import statement to the beginning of the contents contained 
in the file `Bid.sd` containing the SD `Bid`:
```
import Types.*;

sequencediagram Bid {
  ...
}
```
The added import statement means that the file containing the SD imports everything, 
which is stored in the symbol file `Types` (the filw ending `.typesym` is not important 
in this case).
Note that you may have to change the name here, depending on how you named the symbol file from above.
If you strictly followed the instructions of this tutorial, then you are fine.
If we now execute the command again, the CLI tool will print no output, and therefore processed the model successfully, without any context condition violations.
Great! Congratulations on finishing the context condition checking tutorial!

### Storing symbols
In the previous section, we saw how to load symbols from an existing symbol file.
Now, we will use the CLI tool to store a symbol file for our `Bid.sd` model.
The stored symbol file will contain information about the objects defined in the SD.
It can be imported by other models for using the symbols introduced by these definitions.

The `-ss,--storesymbols <arg>` builds the symbol tables of the input models and stores them in the file paths given as arguments.
Either no file paths must be provided or exactly one file path has to be provided for each input model.
The symbol file for the i-th input model is stored in the file defined by the i-th file path. 
If you do not provide any file paths, the CLI tool stores the symbol table of each input model 
in the symbol file `target/symbols/{packageName}/{fileName}.sdsym` 
where `packageName` is the name of the package as specified in the file containing 
the model and `fileName` is the name of the file containing the model. The file is stored relative 
to the working directory, i.e., the directory where you executed the above command.
Furthermore, please notice that in order to store the symbols properly, the model has to be well-formed in all regards, and therefore all context conditions are checked beforehand.


For storing the symbol file of `Bid.sd`, execute the following command:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -ss
```
The CLI tool produces the file `target/symbols/Bid.sdsym` which can now be imported by other models, e.g., which want to use some of the objects defined in the SD `Bid`.

For storing the symbol file of `Bid.sd` in the file `syms/BidSyms.sdsym`, for example, execute the following command:
```
java -jar SD4DevelopmentCLI.jar -i Bid.sd -ss syms/BidSyms.sdsym
```

### Semantic Differencing

Semantic differencing of SDs enables to detect differences in the meanings of the SDs.
The semantic difference from an SD `sd1` to an SD `sd2` is defined as the set of all system runs 
that are valid in `sd1` and not modeled in `sd2`. A system run is a sequence of interactions between
objects. A system run is valid in an SD if the interactions stated in the SD are contained
in the system run in the order specified in the SD and the constraints induced by objects tagged with
stereotypes are satisfied. The system run may contain more interactions than specified
in the SD.
 
In this section we consider the sequence diagrams [rob1.sd](src/test/resources/sddiff/rob1.sd) and [rob2.sd](src/test/resources/sddiff/rob2.sd).
Both SDs are graphically depicted the Figure 2.
Download the files containing the SDs (by using the links) and place them in the directory where the CLI tool `SD4DevelopmentCLI.jar`
is located. 

<img width="600" src="../../../../../../doc/pics/rob1_2.png" alt="The graphical syntax of an example SD" style="float: left; margin-right: 10px;">
<br><b>Figure 2:</b> Two sequence diagrams chosen for semantic differencing.

&nbsp;  

To calculate an element contained in the semantic difference from an SD to another SD, 
the CLI tool provides the `-sd,--semdiff` option.
For example, to calculate an element contained in the semantic difference from
the SD defined in the file `rob2.sd` to the SD defined in the file `rob1.sd`, execute 
the following command:

```
java -jar SD4DevelopmentCLI.jar -sd -i rob2.sd rob1.sd
```

The CLI prints the following output, which represents the interaction sequenec of a system
run that is valid in the SD `rob2` and not valid in the SD `rob1`:
```
Diff witness:
ui -> controller : deliver(r4222,wd40),
controller -> planner : getDeliverPlan(r4222,wd40),
planner -> controller : plan,
planner -> stateProvider : getState(),
controller -> actionExecutor : moveTo(r4222),
actionExecutor -> controller : ACTION_SUCCEEDED
```

However, as the semantic difference operator is by no means commutative, swapping the arguments changes the result.
Execute the following command:
```
java -jar SD4DevelopmentCLI.jar -sd -i rob1.sd rob2.sd
```

This yield the following output:
```
The input SD 'rob1.sd' is a refinement of the input SD 'rob2.sd'
```

In this case, the CLI tool outputs that the SD `rob1.sd` is a refinement of the SD 
`rob2.sd`. This which means that every system run that is valid in the SD `rob1` is 
also valid in the SD `rob2`. Thus, the semantic difference from `rob1` to `rob2` is empty.
