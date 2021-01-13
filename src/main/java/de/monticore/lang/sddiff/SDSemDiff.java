/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import com.google.common.collect.Sets;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.automata.Automaton;
import de.se_rwth.automata.Operations;
import de.se_rwth.automata.State;
import de.se_rwth.automata.Transition;
import de.se_rwth.automata.exceptions.AlphabetsNotEqualException;
import de.se_rwth.commons.logging.Log;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SDSemDiff {

  public Optional<List<SDInteraction>> semDiff(ASTSDArtifact ast1, ASTSDArtifact ast2) {
    SDDiffSDInfoVisitor ast1Info = new SDDiffSDInfoVisitor();
    SDDiffSDInfoVisitor ast2Info = new SDDiffSDInfoVisitor();

    SD4DevelopmentTraverser t1 = SD4DevelopmentMill.traverser();
    SD4DevelopmentTraverser t2 = SD4DevelopmentMill.traverser();

    t1.setSD4DevelopmentHandler(ast1Info);
    t2.setSD4DevelopmentHandler(ast2Info);

    t1.add4SDBasis(ast1Info);
    t1.add4SD4Development(ast1Info);

    t2.add4SDBasis(ast2Info);
    t2.add4SD4Development(ast2Info);

    ast1Info.setTraverser(t1);
    ast2Info.setTraverser(t2);

    ast1.accept(t1);
    ast2.accept(t2);

    Set<SDInteraction> alphabet = getAlphabet(ast1Info, ast2Info);

    Automaton<SDInteraction, Integer> m1 = getNFA(alphabet, ast1Info);
    Automaton<SDInteraction, Integer> m2 = getNFA(alphabet, ast2Info);

    try {
      return Operations.languageIncludedOrWord(m1, m2);
    }
    catch (AlphabetsNotEqualException e) {
      Log.error(e.getMessage());
    }

    return Optional.empty();
  }

  Set<SDInteraction> getAlphabet(SDDiffSDInfoVisitor ast1Info, SDDiffSDInfoVisitor ast2Info) {
    Set<String> omega = new HashSet<>(Sets.union(ast1Info.getObjects(), ast2Info.getObjects()).immutableCopy());
    omega.add("$$__$$"); // some name which doesn't occur in omega

    Set<String> alpha = new HashSet<>(Sets.union(ast1Info.getActions(), ast2Info.getActions()).immutableCopy());
    alpha.add("$$__$$"); // some name which doesn't occur in alpha

    return getAlphabet(omega, alpha);
  }

  private Set<SDInteraction> getAlphabet(Set<String> omega, Set<String> alpha) {
    return Sets.cartesianProduct(omega, alpha, omega).stream().map(e -> new SDInteraction(e.get(0), e.get(1), e.get(2))).collect(Collectors.toSet());
  }

  Automaton<SDInteraction, Integer> getNFA(Set<SDInteraction> alphabet, SDDiffSDInfoVisitor astInfo) {
    final Automaton.Builder<SDInteraction, Integer> nfa = Automaton.nfa(alphabet);
    final int d = astInfo.getInteractions().size();
    Map<Integer, State<Integer>> states = IntStream.rangeClosed(0, d).mapToObj(n -> new State<>(n == 0, n == d, n)).collect(Collectors.toMap(State::getData, Function.identity()));
    states.values().forEach(nfa::addState);
    State<Integer> initialState = states.get(0);
    State<Integer> finalState = states.get(d);
    alphabet.forEach(a -> {
      // L_0
      nfa.addTransition(new Transition<>(initialState, initialState, a));
      // L_|d|
      nfa.addTransition(new Transition<>(finalState, finalState, a));
    });
    // L_k
    for (int k = 1; k < d; k++) {
      Set<SDInteraction> X_k = getX_k(alphabet, astInfo, k, d);
      State<Integer> s_k = states.get(k);
      for (SDInteraction l : Sets.intersection(alphabet, X_k)) {
        nfa.addTransition(new Transition<>(s_k, s_k, l));
      }
    }
    // P_k
    for (int k = 0; k < d; k++) {
      State<Integer> from = states.get(k);
      State<Integer> to = states.get(k + 1);
      SDInteraction a = astInfo.getInteractions().get(k);
      nfa.addTransition(new Transition<>(from, to, a));
    }
    return nfa.build();
  }

  private Set<SDInteraction> getX_k(Set<SDInteraction> I, SDDiffSDInfoVisitor astInfo, int k, int d) {
    final Set<SDInteraction> I_c = I.stream().filter(i -> checkComplete(astInfo, i)).collect(Collectors.toSet());
    final Set<SDInteraction> I_v = I.stream().filter(i -> checkVisible(astInfo, i)).collect(Collectors.toSet());
    final Set<SDInteraction> I_ik = astInfo.getInteractions().subList(k + 1, d).stream().filter(i -> checkInitial(astInfo, i)).collect(Collectors.toSet());
    return Sets.difference(I, Sets.union(Sets.union(I_c, I_v), I_ik));
  }

  private boolean checkComplete(SDDiffSDInfoVisitor astInfo, SDInteraction i) {
    final Set<String> objects = astInfo.getCompleteObjects();
    return objects.contains(i.getSource()) || objects.contains(i.getTarget());
  }

  private boolean checkVisible(SDDiffSDInfoVisitor astInfo, SDInteraction i) {
    final Set<String> objects = astInfo.getVisibleObjects();
    return (objects.contains(i.getSource()) || objects.contains(i.getTarget())) && (astInfo.getObjects().contains(i.getSource()) && astInfo.getObjects().contains(i.getTarget()));
  }

  private boolean checkInitial(SDDiffSDInfoVisitor astInfo, SDInteraction i) {
    final Set<String> objects = astInfo.getInitialObjects();
    return objects.contains(i.getSource()) || objects.contains(i.getTarget());
  }
}
