package de.monticore.lang.sddiff;

import de.se_rwth.automata.Automaton;
import de.se_rwth.automata.Operations;
import de.se_rwth.automata.exceptions.AlphabetsNotEqualException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.monticore.lang.sddiff.SDDiffTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Disabled
public class SDSemDiffAutomataConstructionTest {

  private SDSemDiff sdSemDiff;

  @BeforeEach
  void setup() {
    sdSemDiff = new SDSemDiff();
  }

  @Test
  void testEquivalence_DFA_NFA_rob1_rob2() throws AlphabetsNotEqualException {
    testEquivalence_DFA_NFA(rob1(), rob2());
  }

  @Test
  void testEquivalence_DFA_NFA_rob1_rob3() throws AlphabetsNotEqualException {
    testEquivalence_DFA_NFA(rob1(), rob3());
  }

  @Test
  void testEquivalence_DFA_NFA_rob2_rob3() throws AlphabetsNotEqualException {
    testEquivalence_DFA_NFA(rob2(), rob3());
  }

  private void testEquivalence_DFA_NFA(SequenceDiagram sd1, SequenceDiagram sd2) throws AlphabetsNotEqualException {
    Set<SDInteraction> alphabet = sdSemDiff.getAlphabet(sd1, sd2);

    testEquivalence_DFA_NFA(alphabet, sd1);
    testEquivalence_DFA_NFA(alphabet, sd2);
  }

  private void testEquivalence_DFA_NFA(Set<SDInteraction> alphabet, SequenceDiagram sd) throws AlphabetsNotEqualException {
    Automaton<SDInteraction, Integer> dfa = sdSemDiff.getDFA(alphabet, sd);
    Automaton<SDInteraction, Integer> nfa = sdSemDiff.getNFA(alphabet, sd);

    Optional<List<SDInteraction>> wordNotIncluded = Operations.languageIncludedOrWord(dfa, nfa);
    assertFalse(wordNotIncluded.isPresent(), wordNotIncluded.toString());

    wordNotIncluded = Operations.languageIncludedOrWord(nfa, dfa);
    assertFalse(wordNotIncluded.isPresent(), wordNotIncluded.toString());
  }

}
