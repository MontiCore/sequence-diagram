package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractVisitor implements SDBasisVisitor2 {

  protected ASTCDCompilationUnit compilationUnit;

  protected List<ASTCDElement> classes;

  protected ISD4DevelopmentArtifactScope scope;

  protected final CD4C cd4C;

  protected final GlobalExtensionManagement glex;

  public AbstractVisitor(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    this.compilationUnit = compilationUnit;
    this.classes = classes;
    this.scope = scope;
    this.cd4C = CD4C.getInstance();
    this.glex = glex;
  }

  protected String capitalize(String s) {
    return (s.isEmpty()) ? "" : s.substring(0,1).toUpperCase() + s.substring(1);
  }


  protected String uncapitalize(String s) {
    return (s.isEmpty()) ? "" : s.substring(0,1).toLowerCase() + s.substring(1);
  }

  protected String join(List<String> list) {
    String joiner = "";
    StringBuilder res = new StringBuilder();
    for(String s: list) {
      res.append(joiner).append(s);
      joiner = ",";
    }
    return res.toString();
  }

  protected Collection<String> concat(Collection<String> collection1, Collection<String> collection2) {
    Collection<String> res = new ArrayList<>();
    res.addAll(collection1);
    res.addAll(collection2);
    return res;
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

  public List<ASTCDElement> getClasses() {
    return classes;
  }
}
