package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import fa.State;

public class NFAState extends State {
   

    private Map<Character, Set<NFAState>> transitions;

    public NFAState(String name) {
        super(name); 
        transitions = new HashMap<>();   
    }

    public void addTransition(char symbol, NFAState toState) {
        transitions.putIfAbsent(symbol, new HashSet<>());
        transitions.get(symbol).add(toState);
    }

    public Set<NFAState> getTransitions(char symbol) {
        return transitions.get(symbol); 
    }

    public Map<Character, Set<NFAState>> getTransitionsMap() {
        return transitions;
    }

}
