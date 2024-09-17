/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components;

import com.google.common.base.Preconditions;
import de.monticore.io.FileReaderWriter;
import de.monticore.io.paths.MCPath;
import de.monticore.lang.sd4components._cocos.*;
import de.monticore.lang.sd4components._symboltable.ISD4ComponentsArtifactScope;
import de.monticore.lang.sd4components._symboltable.SD4ComponentsSymbols2Json;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._cocos.*;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SD4ComponentsTool extends SD4ComponentsToolTOP {

  @Override
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

      this.init();
      this.initGlobalScope(cmd);

      runTasks(cmd);
    } catch (ParseException e) {
      // unexpected error from apache CLI parser
      Log.error("0xA7103 Could not process CLI parameters: " + e.getMessage());
    }
  }

  public void runTasks(CommandLine cmd) {
    // disable debug messages
    Log.initWARN();

    // disable fail quick to log as many errors as possible
    Log.enableFailQuick(false);

    // Parse input SDs
    List<ASTSDArtifact> inputSDs = this.parse(".sd", this.createModelPath(cmd).getEntries());

    // pretty print
    if (cmd.hasOption("pp")) {
      if (cmd.getOptionValues("pp") == null || cmd.getOptionValues("pp").length == 0) {
        for (ASTSDArtifact sd : inputSDs) {
          prettyPrint(sd, "");
          System.out.println();
        }
      } else if (cmd.getOptionValues("pp").length != inputSDs.size()) {
        Log.error(String.format("Received '%s' output files for the prettyprint option. "
            + "Expected that '%s' many output files are specified. "
            + "If output files for the prettyprint option are specified, then the number "
            + " of specified output files must be equal to the number of specified input files.",
          cmd.getOptionValues("pp").length, inputSDs.size()));
        return;
      } else {
        for (int i = 0; i < inputSDs.size(); i++) {
          ASTSDArtifact sd_i = inputSDs.get(i);
          prettyPrint(sd_i, cmd.getOptionValues("pp")[i]);
        }
      }
    }

    // Create symbol table
    for (ASTSDArtifact sd : inputSDs) {
      createSymbolTable(sd);
    }

    // cocos
    if (cmd.hasOption("c")) {
      for (ASTSDArtifact sd : inputSDs) {
        runDefaultCoCos(sd);
        runAdditionalCoCos(sd);
      }
    }

    // fail quick in case of symbol storing
    Log.enableFailQuick(true);

    // store symbols
    if (cmd.hasOption("s")) {
      if (cmd.getOptionValues("s") == null || cmd.getOptionValues("s").length == 0) {
        for (int i = 0; i < inputSDs.size(); i++) {
          ASTSDArtifact sd = inputSDs.get(i);
          SD4ComponentsSymbols2Json symbols2Json = new SD4ComponentsSymbols2Json();
          String serialized = symbols2Json.serialize((ISD4ComponentsArtifactScope) sd.getEnclosingScope());

          String fileName = cmd.getOptionValues("i")[i];
          String symbolFile = FilenameUtils.getName(fileName) + "sym";
          String symbol_out = "target/symbols";
          String packagePath = sd.isPresentPackageDeclaration() ? sd.getPackageDeclaration().getQName().replace('.', '/') : "";
          Path filePath = Paths.get(symbol_out, packagePath, symbolFile);
          FileReaderWriter.storeInFile(filePath, serialized);
        }
      } else if (cmd.getOptionValues("s").length != inputSDs.size()) {
        Log.error(String.format("Received '%s' output files for the symboltable option. "
            + "Expected that '%s' many output files are specified. "
            + "If output files for the symboltable option are specified, then the number "
            + " of specified output files must be equal to the number of specified input files.",
          cmd.getOptionValues("s").length, inputSDs.size()));
      } else {
        for (int i = 0; i < inputSDs.size(); i++) {
          ASTSDArtifact sd_i = inputSDs.get(i);
          storeSymbols((ISD4ComponentsArtifactScope) sd_i, cmd.getOptionValues("s")[i]);
        }
      }
    }

    runAdditionalTasks(cmd, inputSDs);
  }

  @Override
  public Options addStandardOptions(Options options) {
    options = super.addStandardOptions(options);
    options.addOption(Option.builder("c").longOpt("coco").desc("Checks the CoCos for the input").build());
    return options;
  }

  @Override
  public void runDefaultCoCos(ASTSDArtifact ast) {
    SD4ComponentsCoCoChecker checker = new SD4ComponentsCoCoChecker();
    // sdbasis cocos
    checker.addCoCo(new CommonFileExtensionCoco());
    checker.addCoCo(new ObjectNameNamingConventionCoco());
    checker.addCoCo(new PackageNameIsFolderNameCoco());
    checker.addCoCo(new SDNameIsArtifactNameCoco());
    checker.addCoCo(new SendMessageHasSourceOrTargetCoco());

    // sd4components cocos
    checker.addCoCo(new UniqueVariableNamingCoco());
    checker.addCoCo(new MessageTypesFitCoCo());
    checker.addCoCo(new MessageTimingFitCoCo());
    checker.addCoCo(new PortUniqueSenderCoCo());

    checker.addCoCo(new ConditionBooleanCoCo());
    checker.addCoCo(new TriggerMessageConcreteCoCo());
    checker.addCoCo(new VariableDeclarationTypesFitCoCo());

    checker.checkAll(ast);
  }

  /**
   * Pretty prints the ast.
   *
   * @param ast  The ast to be printed
   * @param file The file the print is printed to
   */
  @Override
  public void prettyPrint(ASTSDArtifact ast, String file) {
    print(SD4ComponentsMill.prettyPrint(ast, true), file);
  }

  protected void initGlobalScope(CommandLine cl) {
    Preconditions.checkNotNull(cl);
    this.initGlobalScope();
    if (cl.hasOption("path")) {
      String[] paths = this.splitPathEntries(cl.getOptionValue("path"));
      SD4ComponentsMill.globalScope().setSymbolPath(new MCPath(paths));
    }
  }

  public void initGlobalScope() {
    SD4ComponentsMill.globalScope().clear();
    SD4ComponentsMill.globalScope().init();
    BasicSymbolsMill.initializePrimitives();
  }

  /**
   * Splits composedPath on their {@link File#pathSeparator}, e.g. {@code some/path:another/path} on Unix would return
   * {@code {some/path, another/path}} and {@code some\path;other\path} on Windows would return
   * {@code {some\path, other\path}}
   */
  protected final String[] splitPathEntries(String composedPath) {
    Preconditions.checkNotNull(composedPath);

    return composedPath.split(Pattern.quote(File.pathSeparator));
  }

  public final String[] splitPathEntries(String[] composedPaths) {
    Preconditions.checkNotNull(composedPaths);
    return Arrays.stream(composedPaths)
      .map(this::splitPathEntries)
      .flatMap(Arrays::stream)
      .toArray(String[]::new);
  }

  public MCPath createModelPath(CommandLine cl) {
    if (cl.hasOption("i")) {
      return new MCPath(splitPathEntries(cl.getOptionValues("i")));
    } else {
      return new MCPath();
    }
  }

  public List<ASTSDArtifact> parse(String fileExt, Collection<Path> dirs) {
    return dirs.stream()
      .flatMap(directory -> this.parse(fileExt, directory).stream())
      .collect(Collectors.toList());
  }

  public List<ASTSDArtifact> parse(String fileExt, Path directory) {
    try (Stream<Path> paths = Files.walk(directory)) {
      return paths
        .filter(Files::isRegularFile)
        .filter(file -> file.getFileName().toString().endsWith(fileExt))
        .map(Path::toString)
        .map(this::parse)
        .collect(Collectors.toList());
    } catch (IOException e) {
      Log.error("0xA1063 Error while traversing the file structure `" + directory + "`.", e);
    }
    return Collections.emptyList();
  }

  // ------------------
  //  Extension points
  // ------------------

  public void runAdditionalTasks(CommandLine cl, List<ASTSDArtifact> inputSDs) {
    // Extension point
  }

  @Override
  public void runAdditionalCoCos(ASTSDArtifact ast) {
    // Extension point without a warning
  }
}
