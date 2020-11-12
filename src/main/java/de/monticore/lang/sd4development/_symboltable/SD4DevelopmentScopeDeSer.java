/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.io.paths.ModelCoordinate;
import de.monticore.io.paths.ModelCoordinates;
import de.monticore.io.paths.ModelPath;
import de.monticore.utils.Names;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    //packagePath = packagePath.isEmpty() ? "." : packagePath;

    Set<File> foundFiles = new HashSet<>();
    FileFilter fileFilter = new WildcardFileFilter(simpleName + ".*sym");
    for (Path p : modelPath.getFullPathOfEntries()) {
      Path pack = p.resolve(packagePath);
      if (pack.toFile().isDirectory()) {
        foundFiles.addAll(Arrays.asList(pack.toFile().listFiles(fileFilter)));
      }
    }

    if (foundFiles == null || foundFiles.size() == 0) {
      return false;
    }
    else if (foundFiles.size() > 1) {
      Log.error("0x8000 Ambigious results for serialized symboltable: " + packagePath + "/" + simpleName + "*.sym");
      return false;
    }

    Path qualifiedPath = foundFiles.iterator().next().toPath();
    try {
      URL url = qualifiedPath.toUri().toURL();
      enclosingScope.addSubScope(load(url));
      return true;
    }
    catch (MalformedURLException e) {
      return false;
    }
  }
}
