/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._ast.*;
import de.monticore.lang.sd4development._prettyprint.SD4DevelopmentFullPrettyPrinter;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentHandler;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

final class SDDiffSDInfoVisitor implements SD4DevelopmentVisitor2, SDBasisVisitor2, SD4DevelopmentHandler {

  private final SD4DevelopmentFullPrettyPrinter pp = new SD4DevelopmentFullPrettyPrinter(new IndentPrinter());

  // the information required for the trafo to an NFA
  private final Set<String> objects;
  private final Set<String> completeObjects;
  private final Set<String> visibleObjects;
  private final Set<String> initialObjects;
  private final Set<String> actions;
  private final List<SDInteraction> interactions;

  // helper variables used during visiting
  private boolean isComplete;
  private boolean isVisible;
  private boolean isInitial;
  private String currentObject;
  private String currentInteractionSource;
  private String currentInteractionTarget;
  private String currentInteractionAction;

  private SD4DevelopmentTraverser traverser;

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
  }

  @Override
  public SD4DevelopmentTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SD4DevelopmentTraverser traverser) {
    this.traverser = traverser;
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
  public void visit(ASTSDNew node) {
    objects.add(node.getName());
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
    currentInteractionAction = pp.prettyprint(node);
    actions.add(currentInteractionAction);
  }

  @Override
  public void visit(ASTSDCall node) {
    currentInteractionAction = pp.prettyprint(node);
    actions.add(currentInteractionAction);
  }

  @Override
  public void visit(ASTSDReturn node) {
    currentInteractionAction = pp.prettyprint(node);
    actions.add(currentInteractionAction);
  }

  @Override
  public void visit(ASTSDThrow node) {
    currentInteractionAction = pp.prettyprint(node);
    actions.add(currentInteractionAction);
  }

  @Override
  public void endVisit(ASTSDSendMessage node) {
    interactions.add(new SDInteraction(currentInteractionSource, currentInteractionAction, currentInteractionTarget));
  }

  @Override
  public void endVisit(ASTSDNew node) {
    interactions.add(new SDInteraction(currentInteractionSource, currentInteractionAction, currentInteractionTarget));
  }

  @Override
  public void endVisit(ASTSDEndCall node) {
    interactions.add(new SDInteraction(currentInteractionSource, currentInteractionAction, currentInteractionTarget));
  }
}
