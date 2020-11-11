/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development;

import de.monticore.io.FileReaderWriter;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._cocos.*;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentDelegatorVisitor;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._cocos.*;
import de.monticore.lang.sddiff.SDInteraction;
import de.monticore.lang.sddiff.SDSemDiff;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CLI tool providing functionality for processing Sequence Diagram (SD) artifacts.
 */
public class SD4DevelopmentCLI {

  /*
   * Main method of the CLI.
   */
  public static void main(String[] args) {
    SD4DevelopmentCLI cli = new SD4DevelopmentCLI();
    cli.run(args);
  }

  public void run(String[] args) {
    Options options = initOptions();

    CommandLineParser cliparser = new DefaultParser();
    try {
      CommandLine cmd = cliparser.parse(options, args);

      // help or no input
      if (cmd.hasOption("h") || !cmd.hasOption("i")) {
        printHelp(options);
        return;
      }

      // disable debug messages
      Log.initWARN();

      // disable fail quick to log as much errors as possible
      Log.enableFailQuick(false);

      // Parse input FDs
      List<ASTSDArtifact> inputSDs = new ArrayList<>();
      for (String inputFileName : cmd.getOptionValues("i")) {
        Optional<ASTSDArtifact> ast = parseSDArtifact(inputFileName);
        if (!ast.isPresent()) {
          Log.error(String.format("Parsing the input SD '%s' was not successful", inputFileName));
          return;
        }
        inputSDs.add(ast.get());
      }

      // semantic differencing
      if (cmd.hasOption("sd")) {
        if (inputSDs.size() != 2) {
          Log.error(String.format("Received %s input SDs. However, the option 'semdiff' requires exactly two input SDs.", inputSDs.size()));
          return;
        }
        Optional<List<SDInteraction>> witness = semDiff(inputSDs.get(0), inputSDs.get(1));
        if (witness.isPresent()) {
          System.out.println("Diff witness:");
          for (int i = 0; i < witness.get().size() - 1; i++) {
            System.out.println(witness.get().get(i) + ",");
          }
          System.out.println(witness.get().get(witness.get().size() - 1));
        }
        else {
          System.out.println(String.format("The input SD '%s' is a refinement of the input SD '%s'",
            cmd.getOptionValues("i")[0],
            cmd.getOptionValues("i")[1]));
        }
      }

      // pretty print
      if (cmd.hasOption("pp")) {
        if (cmd.getOptionValues("pp") == null || cmd.getOptionValues("pp").length == 0) {
          for (ASTSDArtifact sd : inputSDs) {
            System.out.println(prettyPrint(sd));
            System.out.println();
          }
        }
        else if (cmd.getOptionValues("pp").length != inputSDs.size()) {
          Log.error(String.format("Received '%s' output files for the prettyprint option. "
            + "Expected that '%s' many output files are specified. "
            + "If output files for the prettyprint option are specified, then the number "
            + " of specified output files must be equal to the number of specified input files.",
            cmd.getOptionValues("pp").length,
            inputSDs.size()));
          return;
        }
        else {
          for (int i = 0; i < inputSDs.size(); i++) {
            ASTSDArtifact sd_i = inputSDs.get(i);
            String prettyPrinted = prettyPrint(sd_i);
            FileReaderWriter.storeInFile(Paths.get(cmd.getOptionValues("pp")[i]), prettyPrinted);
          }
        }
      }

      // we need the global scope for symbols and cocos
      ModelPath modelPath = new ModelPath(Paths.get(""));
      if (cmd.hasOption("mp")) {
        modelPath = new ModelPath(Arrays.stream(cmd.getOptionValues("mp")).map(x -> Paths.get(x)).collect(Collectors.toList()));
      }

      ISD4DevelopmentGlobalScope globalScope = SD4DevelopmentMill
        .sD4DevelopmentGlobalScopeBuilder()
        .setModelPath(modelPath)
        .setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION).build();


      // handle CoCos and symbol storage: build symbol table as far as needed
      Set<String> cocoOptionValues = new HashSet<>();
      if(cmd.hasOption("c") && cmd.getOptionValues("c") != null) {
        cocoOptionValues.addAll(Arrays.asList(cmd.getOptionValues("c")));
      }
      if (cmd.hasOption("c") || cmd.hasOption("ss")) {
        for (ASTSDArtifact sd : inputSDs) {
          deriveSymbolSkeleton(sd, globalScope);
        }

        if (cocoOptionValues.isEmpty() || cocoOptionValues.contains("type") || cmd.hasOption("ss")) {
          for (ASTSDArtifact sd : inputSDs) {
            ASTMCQualifiedName packageDeclaration = sd.isPresentPackageDeclaration() ? sd.getPackageDeclaration() : SD4DevelopmentMill.mCQualifiedNameBuilder().build();
            SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(sd.getMCImportStatementList(), packageDeclaration);
            SD4DevelopmentDelegatorVisitor stCompleterVisitor = SD4DevelopmentMill
              .sD4DevelopmentDelegatorVisitorBuilder()
              .setSD4DevelopmentVisitor(stCompleter)
              .build();
            globalScope.accept(stCompleterVisitor);
          }
        }
      }

      // cocos
      if (cmd.hasOption("c") || cmd.hasOption("ss")) {
        if (cmd.hasOption("ss") || cocoOptionValues.isEmpty() || cocoOptionValues.contains("type")) {
          for (ASTSDArtifact sd : inputSDs) {
            checkAllCoCos(sd);
          }
        }
        else if (cocoOptionValues.contains("inter")) {
          for (ASTSDArtifact sd : inputSDs) {
            checkAllExceptTypeCoCos(sd);
          }
        }
        else if (cocoOptionValues.contains("intra")) {
          for (ASTSDArtifact sd : inputSDs) {
            checkIntraModelCoCos(sd);
          }
        }
        else {
          Log.error(String.format("Received unexpected arguments '%s' for option 'coco'"
            + "Possible arguments are 'type', 'inter', and 'intra'. If no argument is specified, "
            + "then 'type' is chosen by default.",
            cocoOptionValues.toString()));
        }
      }

      if(Log.getErrorCount() > 0) {
        // if the model is not well-formed, then stop before generating anything
        return;
      }

      // fail quick in case of symbol storing
      Log.enableFailQuick(true);

      // store symbols
      if (cmd.hasOption("ss")) {
        if (cmd.getOptionValues("ss") == null || cmd.getOptionValues("ss").length == 0) {
          for (int i = 0; i < inputSDs.size(); i++) {
            ASTSDArtifact sd = inputSDs.get(i);
            SD4DevelopmentScopeDeSer deSer = SD4DevelopmentMill.sD4DevelopmentScopeDeSerBuilder().build();
            String serialized = deSer.serialize((SD4DevelopmentArtifactScope) sd.getEnclosingScope());

            String fileName = cmd.getOptionValues("i")[i];
            String symbolFile = FilenameUtils.getName(fileName) + "sym";
            String symbol_out = "target/symbols";
            String packagePath = sd.isPresentPackageDeclaration() ? sd.getPackageDeclaration().getQName().replace('.', '/') : "";
            Path filePath = Paths.get(symbol_out, packagePath, symbolFile);
            FileReaderWriter.storeInFile(filePath, serialized);
          }
        }
        else if (cmd.getOptionValues("ss").length != inputSDs.size()) {
          Log.error(String.format("Received '%s' output files for the storesymbols option. "
            + "Expected that '%s' many output files are specified. "
            + "If output files for the storesymbols option are specified, then the number "
            + " of specified output files must be equal to the number of specified input files.",
            cmd.getOptionValues("ss").length,
            inputSDs.size()));
          return;
        }
        else {
          for (int i = 0; i < inputSDs.size(); i++) {
            ASTSDArtifact sd_i = inputSDs.get(i);
            storeSymbols(sd_i, cmd.getOptionValues("ss")[i]);
          }
        }
      }
    }
    catch (ParseException e) {
      // unexpected error from apache CLI parser
      Log.error("0xA7101 Could not process CLI parameters: " + e.getMessage());
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(80);
    formatter.printHelp("SD4DevelopmentCLI", options);
  }

  private Options initOptions() {
    Options options = new Options();

    // help dialog
    options.addOption(Option.builder("h")
      .longOpt("help")
      .desc("Prints this help dialog.")
      .build());

    // inputs
    options.addOption(Option.builder("i")
      .longOpt("input")
      .hasArgs()
      .desc("Processes the SD input artifacts specified as arguments. At least one input SD is mandatory.")
      .build());

    // pretty print
    options.addOption(Option.builder("pp")
      .longOpt("prettyprint")
      .argName("file")
      .optionalArg(true)
      .numberOfArgs(1)
      .desc("Prints the input SDs to stdout or to the specified files (optional).")
      .build());

    // semantic diff
    options.addOption(Option.builder("sd")
      .longOpt("semdiff")
      .desc("Computes a diff witness contained in the semantic difference from the first input "
        + "SD to the second input SD if one exists and prints it to stdout. Requires exactly two "
        + " SDs as inputs. If no diff witness exists, it prints that the first SD is a refinement "
        + " of the second SD to stdout.")
      .build());

    // cocos
    options.addOption(Option.builder("c")
      .longOpt("coco")
      .optionalArg(true)
      .numberOfArgs(3)
      .desc("Checks the CoCos for the input FDs. Possible arguments are 'intra', "
        + " 'inter', and 'type'. When given the argument 'intra', only the intra-model CoCos are "
        + "checked. When given the argument 'inter', only the intra- and inter-model CoCos are checked. "
        + "When given the argument 'type', all CoCos are checked. When no argument is specified, all "
        + "CoCos are checked by default.")
      .build());

    // store symbols
    options.addOption(Option.builder("ss")
      .longOpt("storesymbols")
      .optionalArg(true)
      .hasArgs()
      .desc("Stores the serialized symbol tables of the input SDs in the specified files. The n-th input "
        + "SD is stored in the file as specified by the n-th argument. If no arguments are given, the "
        + "serialized symbol tables are stored in 'target/symbols/{packageName}/{artifactName}.sdsym' by default.")
      .build());

    // model paths
    options.addOption(Option.builder("mp")
      .longOpt("modelpath")
      .hasArgs()
      .desc("Sets the artifact paths for imported symbols.")
      .build());

    return options;
  }

  /**
   * Parses SD artifacts (*.sd files).
   *
   * @param fileName full-qualified name of the file.
   * @return Non-empty optional iff parsing was successful.
   * @throws IOException If something goes wrong while reading the file.
   */
  public Optional<ASTSDArtifact> parseSDArtifact(String fileName) throws IOException {
    SD4DevelopmentParser parser = new SD4DevelopmentParser();
    return parser.parse(fileName);
  }

  /**
   * Pretty prints the ast and returns the result as String.
   *
   * @param ast The ast to be printed
   * @return Pretty-printed ast.
   */
  public String prettyPrint(ASTSDArtifact ast) {
    SD4DevelopmentDelegatorPrettyPrinter prettyPrinter = new SD4DevelopmentDelegatorPrettyPrinter();
    return prettyPrinter.prettyPrint(ast);
  }

  /**
   * Derives symbols for ast and adds them to the globalScope.
   *
   * @param ast         The ast of the SD.
   * @param globalScope Global scope to which the symbols are added.
   * @return The symbol table for ast while considering globalScope.
   */
  public ISD4DevelopmentArtifactScope deriveSymbolSkeleton(ASTSDArtifact ast, ISD4DevelopmentGlobalScope globalScope) {
    SD4DevelopmentSymbolTableCreatorDelegatorBuilder stCreatorBuilder = SD4DevelopmentMill.sD4DevelopmentSymbolTableCreatorDelegatorBuilder();
    stCreatorBuilder = stCreatorBuilder.setGlobalScope(globalScope);
    SD4DevelopmentSymbolTableCreatorDelegator stCreator = stCreatorBuilder.build();
    return stCreator.createFromAST(ast);
  }

  /**
   * Checks whether ast satisfies the intra-model CoCos.
   *
   * @param ast The ast of the SD.
   */
  public void checkIntraModelCoCos(ASTSDArtifact ast) {
    SD4DevelopmentCoCoChecker checker = new SD4DevelopmentCoCoChecker();
    checker.addCoCo(new CommonFileExtensionCoco());
    checker.addCoCo(new ObjectNameNamingConventionCoco());
    checker.addCoCo(new PackageNameIsFolderNameCoco());
    checker.addCoCo(new SDNameIsArtifactNameCoco());
    checker.addCoCo(new SendMessageHasSourceOrTargetCoco());
    checker.addCoCo(new TypeNamingConventionCoco());
    checker.addCoCo(new UniqueObjectNamingCoco());
    checker.addCoCo(new ConstructorObjectNameNamingConventionCoco());
    checker.addCoCo(new EndCallHasSourceOrTargetCoco());
    checker.addCoCo(new MethodActionRefersToCorrectTargetCoco());
    checker.addCoCo(new ReturnOnlyAfterMethodCoco());
    checker.checkAll(ast);
  }

  /**
   * Checks whether ast satisfies the CoCos not targeting type correctness.
   * This method checks all CoCos except the CoCos, which check that used types (for objects and variables) are defined.
   *
   * @param ast The ast of the SD.
   */
  public void checkAllExceptTypeCoCos(ASTSDArtifact ast) {
    checkIntraModelCoCos(ast);
    SD4DevelopmentCoCoChecker checker = new SD4DevelopmentCoCoChecker();
    checker.addCoCo(new ReferencedObjectSourceDeclaredCoco());
    checker.addCoCo(new ReferencedObjectTargetDeclaredCoco());
    checker.checkAll(ast);
  }

  /**
   * Checks whether ast satisfies all CoCos.
   *
   * @param ast The ast of the SD.
   */
  public void checkAllCoCos(ASTSDArtifact ast) {
    checkAllExceptTypeCoCos(ast);
    SD4DevelopmentCoCoChecker checker = new SD4DevelopmentCoCoChecker();
    checker.addCoCo(new CorrectObjectConstructionTypesCoco());
    checker.addCoCo(new MethodActionValidCoco());
    checker.checkAll(ast);
  }

  /**
   * Stores the symbols for ast in the symbol file filename.
   * For example, if filename = "target/symbolfiles/file.sdsym", then the symbol file corresponding to
   * ast is stored in the file "target/symbolfiles/file.sdsym".
   *
   * @param ast      The ast of the SD.
   * @param filename The name of the produced symbol file.
   */
  public void storeSymbols(ASTSDArtifact ast, String filename) {
    SD4DevelopmentScopeDeSer deSer = SD4DevelopmentMill.sD4DevelopmentScopeDeSerBuilder().build();
    String serialized = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());
    FileReaderWriter.storeInFile(Paths.get(filename), serialized);
  }

  /**
   * Loads the symbols from the symbol file filename and returns the symbol table.
   *
   * @param filename Name of the symbol file to load.
   */
  public ISD4DevelopmentArtifactScope loadSymbols(String filename) {
    SD4DevelopmentScopeDeSer deSer = SD4DevelopmentMill.sD4DevelopmentScopeDeSerBuilder().build();
    return deSer.load(filename);
  }

  /**
   * Checks whether the SD "from" is a refinement of the SD "to".
   * Returns Optional.empty if "from" is a refinement of "to".
   * Returns an element in the semantics of "from" that is no element in the semantics of "to" if
   * "from" is no refinement of "to".
   *
   * @param from SD for which it checked whether it refines the SD "to"
   * @param to   SD for which it is checked whether "from" refines it
   * @return Diff witness contained in the semantic difference from "from" to "to"
   */
  public Optional<List<SDInteraction>> semDiff(ASTSDArtifact from, ASTSDArtifact to) {
    SDSemDiff sdSemDifferencer = new SDSemDiff();
    return sdSemDifferencer.semDiff(from, to);
  }

}
