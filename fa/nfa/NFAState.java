package fa.nfa;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import fa.State;


/**
 * Represents a single state in a Nondeterministic Finite Automaton (NFA).
 * Each NFAState can have multiple transitions for the same input symbol
 * and epsilon transitions (rep via character 'e').
 * The state stores all outgoing transitions in a map from symbols to sets of NFAStates
 * @ author Maria Gomez Baeza, Amy Lee, group 33, section 002
 */

public class NFAState extends State {
   

    private final Map<Character, Set<NFAState>> transitions;

    public NFAState(String name) {
        super(name); 
        transitions = new HashMap<>();   
    }


    /**
     * Adds a transition from this state to another state on a given symbol.
     * 
     * 
     * @param symbol the input symbol for the transition 
     * @param toState the target state
     */
    public void addTransition(char symbol, NFAState toState) {
        transitions.putIfAbsent(symbol, new HashSet<>()); //checks if there is already a set and if not, creates a empty set
        transitions.get(symbol).add(toState);
    }

    /**
     * Returns the set of unmodifiable target states for a given transition symbol.
     * If there is no transition on that symbol, returns an empty set.
     * @param symbol  
     * @return  an unmodifiable set of NFAStates reachable from this state on the given symbol
     */
    public Set<NFAState> toStates(char symbol) {
        Set<NFAState> dest = transitions.get(symbol);
        if (dest == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(dest);
    }

     /**
     * returns the state reached from this state after reading the 
     * symbol. returns null if  no transions exits
     * @param symbol
     * @return the next Nfastate 
     */
    public Set<NFAState> getTransitions(char symbol) {
        return toStates(symbol);
    }


    /**
     *  represents tha map of the transison table that maps to the next NFA state.
     * @return all transitions from this state
     */
    public Map<Character, Set<NFAState>> getTransitionsMap() {
        return transitions;
    }

}
