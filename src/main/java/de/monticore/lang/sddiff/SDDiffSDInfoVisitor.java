/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._visitor.SD4DevelopmentInheritanceVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

final class SDDiffSDInfoVisitor implements SD4DevelopmentInheritanceVisitor {

  private final SD4DevelopmentDelegatorPrettyPrinter pp = new SD4DevelopmentDelegatorPrettyPrinter();

  // the information required for the trafo to an NFA
  private Set<String> objects;
  private Set<String> completeObjects;
  private Set<String> visibleObjects;
  private Set<String> initialObjects;
  private Set<String> actions;
  private List<SDInteraction> interactions;

  // helper variables used during visiting
  private boolean isComplete;
  private boolean isVisible;
  private boolean isInitial;
  private String currentObject;
  private String currentInteractionSource;
  private String currentInteractionTarget;
  private String currentInteractionAction;
  private SDBasisVisitor realThis;

  public SDDiffSDInfoVisitor() {
    this.objects = new HashSet<>();
    this.completeObjects = new HashSet<>();
    this.visibleObjects = new HashSet<>();
    this.initialObjects = new HashSet<>();
    this.actions = new HashSet<>();
    this.interactions = new LinkedList<>();
    this.currentInteractionSource = "";
    this.currentInteractionTarget = "";
    this.currentInteractionAction = "";
    realThis = this;
  }

  public Set<String> getObjects() {
    return objects;
  }

  public Set<String> getCompleteObjects() {
    return completeObjects;
  }

  public Set<String> getVisibleObjects() {
    return visibleObjects;
  }

  public Set<String> getInitialObjects() {
    return initialObjects;
  }

  public Set<String> getActions() {
    return actions;
  }

  public List<SDInteraction> getInteractions() {
    return interactions;
  }

  @Override
  public void visit(ASTSDObject node) {
    currentObject = node.getName();
  }

  @Override
  public void visit(ASTSDCompleteModifier node) {
    isComplete = true;
  }

  @Override
  public void visit(ASTSDVisibleModifier node) {
    isVisible = true;
  }

  @Override
  public void visit(ASTSDInitialModifier node) {
    isInitial = true;
  }

  @Override
  public void endVisit(ASTSDObject node) {
    objects.add(currentObject);
    if (isComplete) {
      completeObjects.add(currentObject);
    }
    if (isVisible) {
      visibleObjects.add(currentObject);
    }
    if (isInitial) {
      initialObjects.add(currentObject);
    }
    isComplete = false;
    isVisible = false;
    isInitial = false;
  }

  @Override
  public void visit(ASTSDObjectSource node) {
    currentInteractionSource = node.getName();
  }

  @Override
  public void visit(ASTSDObjectTarget node) {
    currentInteractionTarget = node.getName();
  }

  @Override
  public void visit(ASTSDAction node) {
    currentInteractionAction = pp.prettyPrint(node);
    actions.add(currentInteractionAction);
  }

  @Override
  public void endVisit(ASTSDInteraction node) {
    interactions.add(new SDInteraction(currentInteractionSource, currentInteractionAction, currentInteractionTarget));
  }
}
