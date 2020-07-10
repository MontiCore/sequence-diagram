package de.monticore.lang.sddiff;

import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class AST2SDTrafo implements SD4DevelopmentVisitor {

  private final SD4DevelopmentDelegatorPrettyPrinter pp = new SD4DevelopmentDelegatorPrettyPrinter();

  private Set<SDObject> objects;

  private Set<SDObject> completeObjects;

  private Set<SDObject> visibleObjects;

  private Set<SDObject> initialObjects;

  private boolean isComplete;

  private boolean isVisible;

  private boolean isInitial;

  private List<SDInteraction> interactions;

  private SDObject.Builder currentObject;

  private SDInteraction.Builder currentInteraction;

  SequenceDiagram toSD(ASTSequenceDiagram ast) {
    objects = new HashSet<>();
    completeObjects = new HashSet<>();
    visibleObjects = new HashSet<>();
    initialObjects = new HashSet<>();
    interactions = new LinkedList<>();
    ast.accept(this);
    return SequenceDiagram.builder()
                          .objects(objects)
                          .completeObjects(completeObjects)
                          .visibleObjects(visibleObjects)
                          .initialObjects(initialObjects)
                          .interactions(interactions)
                          .build();
  }

  @Override
  public void visit(ASTSDObject node) {
    String name = node.getName();
    if (node.isPresentMCObjectType()) {
      name += ":" + node.getMCObjectType().printType(new MCBasicTypesPrettyPrinter(new IndentPrinter()));
    }
    currentObject = SDObject.builder().name(name);
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
    SDObject obj = currentObject.build();
    objects.add(obj);
    if (isComplete) {
      completeObjects.add(obj);
    }
    if (isVisible) {
      visibleObjects.add(obj);
    }
    if (isInitial) {
      initialObjects.add(obj);
    }
    isComplete = false;
    isVisible = false;
    isInitial = false;
  }

  @Override
  public void visit(ASTSDInteraction node) {
    currentInteraction = SDInteraction.builder();
  }

  @Override
  public void visit(ASTSDObjectSource node) {
    currentInteraction.source(getObjectByName(node.getName()));
  }

  @Override
  public void visit(ASTSDObjectTarget node) {
    currentInteraction.target(getObjectByName(node.getName()));
  }

  @Override
  public void visit(ASTSDAction node) {
    currentInteraction.action(SDActionString.builder().action(pp.prettyPrint(node)).build());
  }

  private SDObject getObjectByName(String name) {
    List<SDObject> object = objects.stream().filter(o -> name.equals(o.getName())).collect(Collectors.toList());
    if (object.size() != 1) {
      throw new IllegalStateException("TODO");
    }
    return object.get(0);
  }

  @Override
  public void endVisit(ASTSDInteraction node) {
    interactions.add(currentInteraction.build());
  }
}
