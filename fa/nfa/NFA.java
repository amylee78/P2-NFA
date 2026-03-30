package fa.nfa;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
        if (startState == null) {
            return false;
        }
        if (s == null) {
            s = "";
        }

        Set<NFAState> startClosure = eClosure(startState);
        if (containsFinal(startClosure) && s.isEmpty()) {
            return true;
        }

        // BFS over (state-subset, input-index) configurations.
        Queue<Config> queue = new ArrayDeque<>();
        Set<ConfigKey> visited = new HashSet<>();
        ConfigKey startKey = new ConfigKey(0, startClosure);
        queue.add(new Config(startClosure, 0));
        visited.add(startKey);

        while (!queue.isEmpty()) {
            Config cur = queue.remove();
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

            Set<NFAState> move = new HashSet<>();
            for (NFAState st : cur.states) {
                move.addAll(getToState(st, ch));
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

        while (!stack.isEmpty()) {
            NFAState current = stack.pop();
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
            max = Math.max(max, nextStates.size());

            ConfigKey nextKey = new ConfigKey(cur.index + 1, nextStates);
            if (visited.add(nextKey)) {
                queue.add(new Config(nextStates, cur.index + 1));
            }
        }

        return max;
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

        for (String toName : toStates) {
            if (!states.containsKey(toName)) {
                return false;
            }
        }

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

    private boolean containsFinal(Set<NFAState> subset) {
        for (NFAState st : subset) {
            if (finalStates.contains(st)) {
                return true;
            }
        }
        return false;
    }

    private Set<NFAState> epsilonClosureOfSet(Set<NFAState> subset) {
        if (subset == null || subset.isEmpty()) {
            return Collections.emptySet();
        }
        Set<NFAState> closure = new HashSet<>();
        for (NFAState st : subset) {
            closure.addAll(eClosure(st));
        }
        return closure;
    }

    private static final class Config {
        private final Set<NFAState> states;
        private final int index;

        private Config(Set<NFAState> states, int index) {
            this.states = states;
            this.index = index;
        }
    }

    private static final class ConfigKey {
        private final int index;
        private final Set<NFAState> states;

        private ConfigKey(int index, Set<NFAState> states) {
            this.index = index;
            // Freeze the subset so it is safe to use as a HashSet key.
            this.states = Collections.unmodifiableSet(new HashSet<>(states));
        }

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

        @Override
        public int hashCode() {
            return 31 * index + states.hashCode();
        }
    }
}
