package de.monticore.lang.sd4development;

import de.monticore.io.FileReaderWriter;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.sd4development._cocos.*;
import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentGlobalScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScopeDeSer;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentSymbolTableCreatorDelegator;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._cocos.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tool providing functionality for processing Sequence Diagram (SD) artifacts.
 */
public class SD4DevelopmentTool {

  private static final SD4DevelopmentParser parser = new SD4DevelopmentParser();
  private static final SD4DevelopmentScopeDeSer deSer = SD4DevelopmentMill.sD4DevelopmentScopeDeSerBuilder().build();
  private static final SD4DevelopmentDelegatorPrettyPrinter prettyPrinter = new SD4DevelopmentDelegatorPrettyPrinter();

  /**
   * Parses SD artifacts (*.sd files).
   *
   * @param fileName full-qualified name of the file.
   * @return Non-empty optional iff parsing was successful.
   * @throws IOException If something goes wrong while reading the file.
   */
  public static Optional<ASTSDArtifact> parseSDArtifact(String fileName) throws IOException {
    return parser.parse(fileName);
  }

  /**
   * Parses an SD in its textual syntax given by the String sd.
   *
   * @param sd the SD as string in its textual syntax.
   * @return Non-empty optional iff parsing was successful.
   * @throws IOException If something goes wrong while processing the string.
   */
  public static Optional<ASTSDArtifact> parseSDModel(String sd) throws IOException {
    return parser.parse_String(sd);
  }

  /**
   * Pretty prints the ast and returns the result as String.
   *
   * @param ast The ast to be printed
   * @return Pretty-printed ast.
   */
  public static String prettyPrint(ASTSDArtifact ast) {
    return prettyPrinter.prettyPrint(ast);
  }

  /**
   * Derives the symbol table of ast while considering models contained in model path.
   * The models contained in model path are relevant for the imports of ast.
   *
   * @param ast       The ast of the SD.
   * @param modelPath Considered model path.
   * @return The Symbol table for ast.
   */
  public static SD4DevelopmentArtifactScope deriveSymbols(ASTSDArtifact ast, ModelPath modelPath) {
    SD4DevelopmentGlobalScope globalScope = SD4DevelopmentMill.sD4DevelopmentGlobalScopeBuilder().setModelPath(modelPath).setModelFileExtension(SD4DevelopmentGlobalScope.FILE_EXTENSION).build();
    return deriveSymbols(ast, globalScope);
  }

  /**
   * Derives symbols for ast and adds them to the globalScope.
   *
   * @param ast         The ast of the SD.
   * @param globalScope Global scope to which the symbols are added.
   * @return The symbol table for ast while considering globalScope.
   */
  public static SD4DevelopmentArtifactScope deriveSymbols(ASTSDArtifact ast, SD4DevelopmentGlobalScope globalScope) {
    SD4DevelopmentSymbolTableCreatorDelegator stCreator = SD4DevelopmentMill.sD4DevelopmentSymbolTableCreatorDelegatorBuilder().setGlobalScope(globalScope).build();
    return stCreator.createFromAST(ast);
  }

  /**
   * Derives the symbol table of ast while considering models contained in the given model paths.
   * The models paths are passed as their full qualified names.
   * The models contained in model path are relevant for the imports of ast.
   *
   * @param ast        The ast of the SD.
   * @param modelPaths Full qualified names of the considered model paths.
   * @return The symbol table for ast.
   */
  public static SD4DevelopmentArtifactScope deriveSymbols(ASTSDArtifact ast, String... modelPaths) {
    ModelPath modelPath = new ModelPath(Arrays.stream(modelPaths).map(x -> Paths.get(x)).collect(Collectors.toList()));
    return deriveSymbols(ast, modelPath);
  }

  /**
   * Checks whether ast satisfies the intra-model CoCos with respect to the given global scope.
   *
   * @param ast         The ast of the SD.
   * @param globalScope The given global scope.
   */
  public static void checkIntraModelCoCos(ASTSDArtifact ast, SD4DevelopmentGlobalScope globalScope) {
    deriveSymbols(ast, globalScope);
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
   * Checks whether ast satisfies the CoCos not targeting type correctness with respect to the given global scope.
   * This method checks all CoCos except the CoCos, which check that used types (for objects and variables) are defined.
   *
   * @param ast         The ast of the SD.
   * @param globalScope The given global scope.
   */
  public static void checkAllExceptTypeCoCos(ASTSDArtifact ast, SD4DevelopmentGlobalScope globalScope) {
    checkIntraModelCoCos(ast, globalScope);
    SD4DevelopmentCoCoChecker checker = new SD4DevelopmentCoCoChecker();
    checker.addCoCo(new ReferencedObjectSourceDeclaredCoco());
    checker.addCoCo(new ReferencedObjectTargetDeclaredCoco());
    checker.checkAll(ast);
  }

  /**
   * Checks whether ast satisfies all CoCos with respect to the given global scope.
   *
   * @param ast         The ast of the SD.
   * @param globalScope The given global scope.
   */
  public static void checkAllCoCos(ASTSDArtifact ast, SD4DevelopmentGlobalScope globalScope) {
    checkAllExceptTypeCoCos(ast, globalScope);
    SD4DevelopmentCoCoChecker checker = new SD4DevelopmentCoCoChecker();
    checker.addCoCo(new ReferencedTypeExistsCoco());
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
  public static void storeSymbols(ASTSDArtifact ast, String filename) {
    String serialized = deSer.serialize((SD4DevelopmentArtifactScope) ast.getEnclosingScope());
    FileReaderWriter.storeInFile(Paths.get(filename), serialized);
  }

  /**
   * Stores the symbol file for ast at the passed path. The name of the file is calculated by the DeSer, i.e.,
   * the file name has the usual file name of symbol files.
   * For example, an SD defined in the artifact "Bid.sd" is stored in the file "Bid.sdsym".
   * If path = "target/symbols", then the full qualified name of the symbol file is "target/symbols/Bid.sdsym".
   *
   * @param ast  The ast of the SD for which the symbols should be stored.
   * @param path The path where the symbol file should be stored.
   */
  public static void storeSymbols(ASTSDArtifact ast, Path path) {
    deSer.store((SD4DevelopmentArtifactScope) ast.getEnclosingScope(), path);
  }

  /**
   * Loads the symbols from thesymbol file filename and returns the symbol table.
   *
   * @param filename Name of the symbol file to load.
   */
  public static SD4DevelopmentArtifactScope loadSymbols(String filename) {
    return deSer.load(filename);
  }
}
