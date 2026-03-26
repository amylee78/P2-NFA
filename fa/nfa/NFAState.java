package fa.nfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import fa.State;

public class NFAState extends State {
    //to be written

       private Map<Character, Set<NFAState>> transitions;

      public NFAState(String name) {
        super(name); 
        transitions = new HashMap<>(); 
    }


}
