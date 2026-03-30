package fa.nfa;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import fa.State;

public class NFAState extends State {
   

    private final Map<Character, Set<NFAState>> transitions;

    public NFAState(String name) {
        super(name); 
        transitions = new HashMap<>();   
    }

    public void addTransition(char symbol, NFAState toState) {
        transitions.putIfAbsent(symbol, new HashSet<>());
        transitions.get(symbol).add(toState);
    }

    /**
     * Returns the set of destination states for a given transition symbol.
     * If there is no transition on that symbol, returns an empty set.
     */
    public Set<NFAState> toStates(char symbol) {
        Set<NFAState> dest = transitions.get(symbol);
        if (dest == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(dest);
    }

    public Set<NFAState> getTransitions(char symbol) {
        return toStates(symbol);
    }

    public Map<Character, Set<NFAState>> getTransitionsMap() {
        return transitions;
    }

}
