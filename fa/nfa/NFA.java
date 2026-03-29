package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import fa.FAInterface;
import fa.State;

public class NFA  implements FAInterface {

    private Set<NFAState> Q;
    private Set<Character> sigma;
    private Set<NFAState> F;
    private Map<String, NFAState> stateMap;
    private NFAState q0;

    public NFA(){
        Q = new LinkedHashSet<>();
        sigma = new LinkedHashSet<>();
        F = new LinkedHashSet<>();
        stateMap = new HashMap<>();
        q0 = null;
    }

    @Override
    public boolean addState(String name) {
        if (stateMap.containsKey(name)) {
            return false;
            }
        NFAState s = new NFAState(name);
        Q.add(s);
        stateMap.put(name, s);
        return true;
    }

    @Override
    public boolean setFinal(String name) {
      NFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        F.add(state);
        return true;
    }

    @Override
    public boolean setStart(String name) {
        NFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        q0 = state;
        return true;
    }

    @Override
    public void addSigma(char symbol) {
       sigma.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public State getState(String name) {
       return stateMap.get(name);
    }

    @Override
    public boolean isFinal(String name) {
       NFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        // Check if the state is in the F (final states) set
        return F.contains(state);
    }

    @Override
    public boolean isStart(String name) {
          NFAState state = stateMap.get(name);
        if (state == null || q0 == null) {
            return false;
        }
        return state.equals(q0);
    }

    public boolean addTransition(String fromState,  Set<String> toStates, char onSymb) {
        NFAState from = stateMap.get(fromState);
        
        //validate states
        if (from == null) {
            return false;
        }

        // Validate symbols. allow espion e and symbol must be in alphabet
        if (onSymb != 'e' && !sigma.contains(onSymb)) {
            return false;
        }

        // Adds the transition
         for (String stateName : toStates) {
            NFAState to = stateMap.get(stateName);
              if (to == null) {
                return false;
            }
             from.addTransition(onSymb, to);

         }
        
         return true;
    }

    public int maxCopies(String input) {
        //todo
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

      public Set<NFAState> eClosure(NFAState s) {
         //todo
          Set<NFAState> closure = new HashSet<>();
          Stack<NFAState> stack = new Stack<>();
          
          //start Depth first search
          stack.push(s);

          while  (!stack.isEmpty()){
            // pop current state from stack
             NFAState currentState = stack.pop();

             if (!closure.contains(currentState))
            {
                //add current state to closure
                closure.add(currentState);

            }
          }

         return closure;
      } 
      
      public boolean isDFA() {
         //todo
        throw new UnsupportedOperationException("Unimplemented method 'isDFA'");
      }

      public String toString() 
      {
         StringBuilder sb = new StringBuilder();

         // Q = {states}
         sb.append("Q = { ");
         boolean first = true;
          for (NFAState state : Q) {
            if (!first) {
                sb.append(" ");
            }
            sb.append(state.getName());
            first = false;
        }
        sb.append(" }\n");

        //Sigma = { symbols}
        sb.append("Sigma = { ");
        first = true;
        for (Character c : sigma) {
            if (!first) {
                sb.append(" ");
            }
            sb.append(c);
            first = false;
        }
        sb.append(" }\n");

         // delta = transition table
        sb.append("delta =\n\t");

        for ( NFAState state: Q){
            // need to write more
        }

        return sb.toString();

      }

    


}
