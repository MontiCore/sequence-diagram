package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelCoordinate;
import de.monticore.io.paths.ModelCoordinates;
import de.monticore.io.paths.ModelPath;
import de.monticore.utils.Names;
import jdk.internal.jline.internal.Log;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class SD4DevelopmentScopeDeSer extends SD4DevelopmentScopeDeSerTOP {

  /*
   * This method is overridden, to enable deserialization of stored symbol tables with different file extensions.
   * For instance, a serialized class diagram, with file extension "example.cdsym" should be deserializable by the
   * sequence diagram deserializer
   */
  @Override
  public boolean loadSymbolsIntoScope(String qualifiedModelName, ISD4DevelopmentGlobalScope enclosingScope, ModelPath modelPath) {
    //1. Calculate qualified path of of stored artifact scopes relative to model path entries
    String simpleName = Names.getSimpleName(qualifiedModelName);
    String packagePath = Names.getPathFromQualifiedName(qualifiedModelName);
    packagePath = packagePath.isEmpty() ? "." : packagePath;
    File dir = Paths.get(packagePath).toFile();
    FileFilter fileFilter = new WildcardFileFilter(simpleName + ".*sym");
    File[] files = dir.listFiles(fileFilter);

    if (files == null || files.length == 0) {
      return false;
    } else if (files.length > 1) {
      Log.error("Ambigious results, multiple files found (" + files.length + ")");
    }

    Path qualifiedPath = files[0].toPath();

    //2. try to find qualified path within model path entries
    ModelCoordinate modelCoordinate = ModelCoordinates.createQualifiedCoordinate(qualifiedPath);
    modelPath.resolveModel(modelCoordinate);

    //3. Load symbol table into enclosing global scope if a file has been found
    if (modelCoordinate.hasLocation()) {
      URL url = modelCoordinate.getLocation();
      enclosingScope.addSubScope(load(url));
      return true;
    }
    return false;
  }
}
