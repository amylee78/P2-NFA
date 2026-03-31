package fa.nfa;

import java.io.ObjectInputFilter.Config;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *  Implementation of a Non-deterministic Finite Automaton (NFA).
 * An NFA is defined by the 5-tuple (Q, Σ, δ, q0, F) where:
 * - states is a finite set of states
 * - sigma is a finite set of alphabet
 * - δ is the transition function: Q × Σ  → P(Q)
 *       (maps a state and input symbol, or epsilon, to a set of states)
 * - startState is the start state
 * - Finalstate is the set of final/accepting states
 *
 * allows transistions to mutiple states and  epsilon transitions. 
 * Uses BFS to go through the NFA step by step for an input string 
 * and DFS to find all states reachable via ε.
 * 
 * @ author Maria Gomez Baeza, Amy Lee, group 33, section 002
 */

public class NFA implements NFAInterface {

    private final Map<String, NFAState> states;
    private final Set<Character> sigma;
    private final Set<NFAState> finalStates;
    private NFAState startState;

    public NFA() {
        this.states = new HashMap<>();
        this.sigma = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.startState = null;
    }

    @Override
    public boolean addState(String name) {
        if (name == null) {
            return false;
        }
        //rejects any duplicates. return false if  map already holds a state with this name
        if (states.containsKey(name)) {
            return false;
        }
        states.put(name, new NFAState(name));
        return true;
    }

    @Override
    public boolean setFinal(String name) {
        NFAState s = states.get(name);
       
        if (s == null) {
            return false;
        }
        finalStates.add(s);
        return true;
    }

    @Override
    public boolean setStart(String name) {
        NFAState s = states.get(name);
        if (s == null) {
            return false;
        }
        startState = s;
        return true;
    }

    @Override
    public void addSigma(char symbol) {
        if (symbol == 'e') {
            // 'e' is reserved for epsilon transitions and never part of Sigma.
            return;
        }
        sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        
        //reject if there is no start state.
        if (startState == null) {
            return false;
        }
        if (s == null) {
            s = "";
        }
        // Compute the epsilon closure of start state
        Set<NFAState> startClosure = eClosure(startState);

        // the input is empty, the empty string is accepted immediately
        if (containsFinal(startClosure) && s.isEmpty()) {
            return true;
        }

        // BFS over (state-subset, input-index) configurations.
        Queue<Config> queue = new ArrayDeque<>();
        Set<ConfigKey> visited = new HashSet<>(); //prevents visiting the same pair
        ConfigKey startKey = new ConfigKey(0, startClosure);
        queue.add(new Config(startClosure, 0));
        visited.add(startKey);

        while (!queue.isEmpty()) {
            Config cur = queue.remove();

            // All input has been consumed — check if any active state is accepting
            if (cur.index == s.length()) {
                if (containsFinal(cur.states)) {
                    return true;
                }
                continue;
            }

            char ch = s.charAt(cur.index);
            // Epsilon is reserved and never consumed as input.
            if (ch == 'e' || !sigma.contains(ch)) {
                continue;
            }

            // Compute all states reachable from current states on symbol ch
            Set<NFAState> move = new HashSet<>();
            for (NFAState st : cur.states) {
                move.addAll(getToState(st, ch)); //adds transiston for each active state
            }
            Set<NFAState> nextStates = epsilonClosureOfSet(move);
            
            ConfigKey nextKey = new ConfigKey(cur.index + 1, nextStates);
            if (visited.add(nextKey)) {
                queue.add(new Config(nextStates, cur.index + 1));
            }
        }

        return false;
    }

    @Override
    public Set<Character> getSigma() {
        return Collections.unmodifiableSet(sigma);
    }

    @Override
    public NFAState getState(String name) {
        return states.get(name);
    }

    @Override
    public boolean isFinal(String name) {
        NFAState s = states.get(name);
        return s != null && finalStates.contains(s);
    }

    @Override
    public boolean isStart(String name) {
        NFAState s = states.get(name);
        return s != null && s == startState;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        if (from == null) {
            return Collections.emptySet();
        }
        return from.toStates(onSymb);
    }

    /**
     * Epsilon closure via DFS using a stack in a loop.
     * Epsilon transitions are labeled with the reserved character 'e'.
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        if (s == null) {
            return Collections.emptySet();
        }

        Set<NFAState> visited = new HashSet<>();
        ArrayDeque<NFAState> stack = new ArrayDeque<>();
        stack.push(s);

        //Loop through configurations until there are no more configurations left to explore.
        while (!stack.isEmpty()) {
            NFAState current = stack.pop();
            //skip any states that has been visited 
            if (!visited.add(current)) {
                continue;
            }

            // Push all epsilon-reachable children.
            for (NFAState child : getToState(current, 'e')) {
                stack.push(child);
            }
        }

        return visited;
    }

    @Override
    public int maxCopies(String s) {
        if (startState == null) {
            return 0;
        }
        if (s == null) {
            s = "";
        }

        Set<NFAState> startClosure = eClosure(startState);
        int max = startClosure.size();

        //Uses BFS to explore all possible sets of active states at each step of the input.
        Queue<Config> queue = new ArrayDeque<>();
        Set<ConfigKey> visited = new HashSet<>();
        ConfigKey startKey = new ConfigKey(0, startClosure);
        queue.add(new Config(startClosure, 0));
        visited.add(startKey);

        while (!queue.isEmpty()) {
            Config cur = queue.remove();
            if (cur.index == s.length()) {
                continue;
            }

            char ch = s.charAt(cur.index);
            
            if (ch == 'e' || !sigma.contains(ch)) {
                continue;
            }

            Set<NFAState> move = new HashSet<>();
            for (NFAState st : cur.states) {
                move.addAll(getToState(st, ch));
            }
            Set<NFAState> nextStates = epsilonClosureOfSet(move);
            
            max = Math.max(max, nextStates.size()); //update if umber of active states in nextStates is larger than the current maximum

            //Adds new config if unvisted
            ConfigKey nextKey = new ConfigKey(cur.index + 1, nextStates);
            if (visited.add(nextKey)) {
                queue.add(new Config(nextStates, cur.index + 1));
            }
        }

        return max; //return results
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        if (fromState == null || toStates == null) {
            return false;
        }

        // Allow epsilon transitions; otherwise require the symbol to be in Sigma.
        if (onSymb != 'e' && !sigma.contains(onSymb)) {
            return false;
        }

        NFAState from = states.get(fromState);
        if (from == null) {
            return false;
        }
        // Validate all destination state names before adding any transitions
        for (String toName : toStates) {
            if (!states.containsKey(toName)) {
                return false;
            }
        }
        // Add transition from source to all target states
        for (String toName : toStates) {
            from.addTransition(onSymb, states.get(toName));
        }
        return true;
    }

    @Override
    public boolean isDFA() {
        // DFA cannot have epsilon transitions and must be deterministic for each Sigma symbol.
        for (NFAState st : states.values()) {
            if (!st.toStates('e').isEmpty()) {
                return false;
            }
            for (char sym : sigma) {
                if (st.toStates(sym).size() != 1) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * helper method that  checks if a set contains any final accepting state
     * 
     *  @param subset the set of NFAState objects to check
     * @return true if at least one state in the set is final; false otherwise
     */
    private boolean containsFinal(Set<NFAState> subset) {
        for (NFAState st : subset) {
            if (finalStates.contains(st)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the epsilon closure of a set of states.
     * Returns an empty set for a null or empty input.
     *
     * @param subset  - the set of NFAState objects to check
     * @return true if at least one state in the set is final; false otherwise
     */
    private Set<NFAState> epsilonClosureOfSet(Set<NFAState> subset) {
        if (subset == null || subset.isEmpty()) {
            return Collections.emptySet();
        }
        // iterates through each state in the subset
        Set<NFAState> closure = new HashSet<>();
        for (NFAState st : subset) {
            closure.addAll(eClosure(st));
        }
        return closure;
    }

    /**
     * a helper method that represents a BFS  during NFA traversal. 
     * Contains the the set of  active NFA states and the 
     *  index of the input string being processed.
     *  index is how many characters of the input have been consumed so far
     */
    private static final class Config {
        private final Set<NFAState> states;
        private final int index;

        private Config(Set<NFAState> states, int index) {
            this.states = states;
            this.index = index;
        }
    }
    /**
     *  a key used for tracking visited states in BFS to avoid any revisiting
     * based on the current input index and the set of active states
     * 
     */
    private static final class ConfigKey {
        private final int index;
        private final Set<NFAState> states; //active states at the indext

        private ConfigKey(int index, Set<NFAState> states) {
            this.index = index;
            // Freeze the subset so it is safe to use as a HashSet key.
            this.states = Collections.unmodifiableSet(new HashSet<>(states));
        }

        /**
         * compares the ConfigKey with another aobject for equality
         * they are equal if  both the input index and the  set of active states are identical
         * used to track visited states in BFS to avoid doing it twice
         * 
         * @param obj the object to compare with this ConfigKey
         *  @return true if the given object is equal to this ConfigKey, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConfigKey)) {
                return false;
            }
            ConfigKey other = (ConfigKey) obj;
            return index == other.index && states.equals(other.states);
        }

        /**
         * returns a hash  value for the ConfigKey
         *  Combines the input index and the hash code of the active state set.
         * used to make sure ConfigKey can be safetly used HashSet and HashMap
         */
        @Override
        public int hashCode() {
            return 31 * index + states.hashCode();
        }
    }
}
