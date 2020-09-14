/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sddiff;

import com.google.common.collect.Sets;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.se_rwth.automata.*;
import de.se_rwth.automata.exceptions.AlphabetsNotEqualException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SDSemDiff {

  private static final Logger log = LoggerFactory.getLogger(SDSemDiff.class);
  private static final AST2SDTrafo trafo = new AST2SDTrafo();

  public Optional<SDSemDiffWitness> semDiff(ASTSDArtifact ast1, ASTSDArtifact ast2) {
    SequenceDiagram sd1 = trafo.toSD(ast1);
    SequenceDiagram sd2 = trafo.toSD(ast2);
    return semDiff(sd1, sd2);
  }

  public Optional<SDSemDiffWitness> semDiff(SequenceDiagram sd1, SequenceDiagram sd2) {
    log.debug("Started Semantic Difference of Sequence Diagrams");
    Set<SDInteraction> alphabet = getAlphabet(sd1, sd2);

    Automaton<SDInteraction, Integer> m1 = getNFA(alphabet, sd1);
    Automaton<SDInteraction, Integer> m2 = getNFA(alphabet, sd2);

    log.debug("Finished translation from Sequence Diagrams to NFAs. Checking language inclusion.");
    try {
      Optional<List<SDInteraction>> witness = Operations.languageIncludedOrWord(m1, m2);
      if (witness.isPresent()) {
        return Optional.of(SDSemDiffWitness.builder()
                                           .witness(witness.get())
                                           .build());
      }
    } catch (AlphabetsNotEqualException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  Set<SDInteraction> getAlphabet(SequenceDiagram sd1, SequenceDiagram sd2) {
    Set<SDObject> omega = new HashSet<>(Sets.union(sd1.getObjects(), sd2.getObjects()).immutableCopy());
    omega.add(SDObject.builder().name("$$__$$").build()); // some name which doesn't occur in omega

    Set<SDAction> alpha = new HashSet<>(Sets.union(getActions(sd1.getInteractions()), getActions(sd2.getInteractions()))
                                            .immutableCopy());
    alpha.add(SDActionString.builder().action("$$__$$").build()); // some name which doesn't occur in alpha

    return getAlphabet(omega, alpha);
  }

  private Set<SDAction> getActions(List<SDInteraction> interactions) {
    return interactions.stream().map(SDInteraction::getAction).collect(Collectors.toSet());
  }

  private Set<SDInteraction> getAlphabet(Set<SDObject> omega, Set<SDAction> alpha) {
    return Sets.cartesianProduct(omega, alpha, omega).stream()
               .map(e -> new SDInteraction((SDObject) e.get(0), (SDAction) e.get(1), (SDObject) e.get(2)))
               .collect(Collectors.toSet());
  }

  Automaton<SDInteraction, Integer> getDFA(Set<SDInteraction> alphabet, SequenceDiagram sd) {
    final Automaton.Builder<SDInteraction, Integer> dfa = Automaton.dfa(alphabet);
    final int d = sd.getInteractions().size();
    Map<Integer, State<Integer>> states = IntStream.rangeClosed(0, d+1)
                                                   .mapToObj(n -> new State<>(n == 0, n == d, n))
                                                   .collect(Collectors.toMap(State::getData, Function.identity()));
    List<Transition<SDInteraction, Integer>> transitions = new ArrayList<>();
    states.values().forEach(dfa::addState);
    State<Integer> initialState = states.get(0);
    State<Integer> finalState = states.get(d);
    State<Integer> catchState = states.get(d+1);
    alphabet.forEach(a -> {
      // L_0
      if (!sd.getInteractions().get(0).equals(a)) {
        transitions.add(new Transition<>(initialState, initialState, a));
      }
      // L_|d|
      transitions.add(new Transition<>(finalState, finalState, a));
      // catch state self loop
      transitions.add(new Transition<>(catchState, catchState, a));
    });
    // L_k
    for (int k = 1; k < d; k++) {
      Set<SDInteraction> X_k = getX_k(alphabet, sd, k, d);
      State<Integer> s_k = states.get(k);
      for (SDInteraction l : Sets.difference(X_k, Sets.newHashSet(sd.getInteractions().get(k)))) {
        transitions.add(new Transition<>(s_k, s_k, l));
      }
    }
    // P_k
    for (int k = 0; k < d; k++) {
      State<Integer> from = states.get(k);
      State<Integer> to = states.get(k + 1);
      SDInteraction a = sd.getInteractions().get(k);
      transitions.add(new Transition<>(from, to, a));
    }
    transitions.forEach(dfa::addTransition);
    List<Transition<SDInteraction, Integer>> remaining = new ArrayList<>();
    // add remaining transitions to catch state
    for (State<Integer> s : states.values()) {
      for (SDInteraction i : alphabet) {
        if (transitions.stream().noneMatch(t -> s.equals(t.getFrom()) && i.equals(t.getSymbol()))) {
          remaining.add(new Transition<>(s, catchState, i));
        }
      }
    }
    remaining.forEach(dfa::addTransition);
    return dfa.build();
  }

  Automaton<SDInteraction, Integer> getNFA(Set<SDInteraction> alphabet, SequenceDiagram sd) {
    final Automaton.Builder<SDInteraction, Integer> nfa = Automaton.nfa(alphabet);
    final int d = sd.getInteractions().size();
    Map<Integer, State<Integer>> states = IntStream.rangeClosed(0, d)
                                                   .mapToObj(n -> new State<>(n == 0, n == d, n))
                                                   .collect(Collectors.toMap(State::getData, Function.identity()));
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
      Set<SDInteraction> X_k = getX_k(alphabet, sd, k, d);
      State<Integer> s_k = states.get(k);
      for (SDInteraction l : Sets.intersection(alphabet, X_k)) {
        nfa.addTransition(new Transition<>(s_k, s_k, l));
      }
    }
    // P_k
    for (int k = 0; k < d; k++) {
      State<Integer> from = states.get(k);
      State<Integer> to = states.get(k + 1);
      SDInteraction a = sd.getInteractions().get(k);
      nfa.addTransition(new Transition<>(from, to, a));
    }
    return nfa.build();
  }

  private Set<SDInteraction> getX_k(Set<SDInteraction> I, SequenceDiagram sd, int k, int d) {
    final Set<SDInteraction> I_c = I.stream()
                                    .filter(i -> checkComplete(sd, i))
                                    .collect(Collectors.toSet());
    final Set<SDInteraction> I_v = I.stream()
                                    .filter(i -> checkVisible(sd, i))
                                    .collect(Collectors.toSet());
    final Set<SDInteraction> I_ik = sd.getInteractions()
                                      .subList(k + 1, d)
                                      .stream()
                                      .filter(i -> checkInitial(sd, i))
                                      .collect(Collectors.toSet());
    return Sets.difference(I, Sets.union(Sets.union(I_c, I_v), I_ik));
  }

  private boolean checkComplete(SequenceDiagram sd, SDInteraction i) {
    final Set<SDObject> objects = sd.getCompleteObjects();
    return objects.contains(i.getSource()) || objects.contains(i.getTarget());
  }

  private boolean checkVisible(SequenceDiagram sd, SDInteraction i) {
    final Set<SDObject> objects = sd.getVisibleObjects();
    return (objects.contains(i.getSource()) || objects.contains(i.getTarget())) &&
      (sd.getObjects().contains(i.getSource()) && sd.getObjects().contains(i.getTarget()));
  }

  private boolean checkInitial(SequenceDiagram sd, SDInteraction i) {
    final Set<SDObject> objects = sd.getInitialObjects();
    return objects.contains(i.getSource()) || objects.contains(i.getTarget());
  }
}
