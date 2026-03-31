****************
* P2: Nondeterministic Finite Automata
* CS361, Theory of comp
* 03/30/2026
*  Maria Gomez Baeza, Amy Lee, group 33, section 002
**************** 


## OVERVIEW

This project implements a Nondeterministic Finite Automaton (NFA) in Java  that models the behavior of a NFA such as using empty string transion(eClosure) and mutiple transions from a single state.


## INCLUDED FILES
 * NFAInterface.java - Extends FAInterface. Defines required behavior for a NFA.
 * FaInterface.java - Defines the behavior of a Finite Automona
 * State.java - Abstract class for states
 * NFA.java - Implements the logic of the NFA such as  states, transitions, eClosure, and max copies. 
 * NFAState.java - implements a single state in the NFA. Extends state.java 
 * NFATest.java - Test file that instantiates an NFA and checks for expected behavior.
 * hamcrest-core-1.3.jar - A JUnit testing framework library
 * junit-4.13.2.jar - A library used by JUnit to check conditions in tests such as assertTrue
 * README - this file

## COMPILING AND RUNNING  //to be corrected

To compile and run this project, please run the following commands

        1. Compile all java files in the current directory. Makes sure it is in the root directory first: 
             - javac fa/**/*.java
        2. Then to test out the code  in your terminal on your top directly use this command to compile the DFA.test
            -  javac -cp .:/usr/share/java/junit.jar ./test/nfa/NFATest.java
        3. use this to run the test
            -  java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/hamcrest.jar
org.junit.runner.JUnitCore test.nfa.NFATest

        4. If running into such as having junit5. Please use these commands once you are in the root file to compile it
            - javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar test/nfa/NFATest.java 
        5. then to run it use this command 
            -  java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.nfa.NFATest


once done, the output should have a output like when sucessful:  
nfa1 <test> done


## PROGRAM DESIGN AND IMPORTANT CONCEPTS

 This project models  a instance of an Nondeterministic Finite Automaton (NFA) and its behavior. NFAs are one of the Finite Automata 
 abstract machines used to reconize patterns in a input sequence.  NFA consists of a  5-tuple that represent the states, alphabet, 
 transition  start  and final states. SSimilar to DFA, NFA allows transitions to mutiple states for one input and allows empty 
 string transion: " ε " , represented via "e" in this program. This allows to move the input symbol without consuming any string. The NFA processes input using BFS to track all active states, while eClosure uses DFS with a stack to find all states reachable via ε-transitions

### Main concepts and organization
   -  NFAState - This class represents a single State in NFA that stores its transitions as Map<Character, Set<NFAState>> transitions  from input symbols( aka the alphabet : a, 1, 0, b etc) to the target states, allowing mutiple transitions for the same symbol. It supports ε-transitions via the character 'e'.

   - NFA - this class manages NFA as a whole, it 
        - Tracks all states and the alphabet
        -  maintains start and final state
        - impelment  graph search algorithms : DFS and BFS
        - mantaints adding states and transitions
    
#### Key concepts 
  - eClosure- computes the set of all NFA states reachable from s (starting state) through ε-transitions using depth-first search (DFS) with a stack. It uses DFS to explore states that are reachable by ε and pushing it into the stack. It adds it to the visited state and pushes any unvisted states into the stack into all reachable states are found.
 - Breadth-First Search (BFS) - Simulates the NFA on an input string, keeping track of the current set of active states and input index using a queue of Config objects

 - max Copies – finds the largest number of NFA states that can be “active” at the same time while reading a given input string. It uses BFS to simulate the NFA step by step. aka the worst case senerio

 - DFA check - Determines if the NFA is deterministic by verifying the absence of ε-transitions and ensuring each symbol leads to exactly one state.
        
 - Config and ConfigKey – Helper classes is used to stimulate BFS. Config stores the current active states and input index. ConfigKey is used to track visited configurations to avoid any redundant processing.


## DISCUSSION
 
During development, the biggest issue was translating the NFA “multiple copies” idea into concrete Java data structures and algorithms. I initially had the method skeletons compiling, but most tests failed because the real challenge is correctly simulating nondeterminism while also merging identical copies. We ran into logical erros like, treating 'e' as if it could be consumed from the input string, forgetting to apply ε-closure at the start or after reading each symbol, which caused acceptance to be wrong, and accidentally counting duplicates as extra copies. The most challenging parts were implementinge closure correctly using DFS with a stack and a loop, while avoiding infinite loops in cyclic ε-transitions and accepts and maxCopies, because it was easy to lose track of when to apply closure and how to correctly track the evolving set of states. It finally clicked when I treated NFA simulation as repeatedly applying epsilon-closure before and after each move. Once I followed that pattern, my results matched JUnit, and the remaining issues were just small edge cases.
 
 
## TESTING

To make sure our progam works, we used the given JUnit which allows us to verify that the NFA behaves correctly under various scenarios such as invoking a exlosure, if NFA is a DFA, passing string to NFA and checking the max number. The testing stragegty was to get he skeleton of the program done and run the tests. Then go through each failed test one by one. We looked at the test results and problems to see what kind of issues were having such as  "The method eClosure(NFAState) in the type NFA is not applicable for the arguments (State) " . At any places where the code breaks, we added a breakpoint and steped over and through to see what is expected and what we are getting. we then attempted to fix it based on the error and ran the test again until everything was passing.

## SOURCES

https://docs.oracle.com/javase/7/docs/api/java/util/Set.html  - used to help  impelment  correct data structures for each of NFA’s element such as eclosure.

https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html  - a faster implmentation then linked hashmap. As in NFA, the mutipel transitiosn and order of visited states doesnt matter, hashamap is more reliable then linked hashmap

https://docs.oracle.com/en/middleware/standalone/coherence/15.1.1/java-reference/com/oracle/coherence/rag/config/ConfigKey.html   - used to help with the helper ConfigKey to uniquely identify a configuration and to help track visted  configuration. Useuful for BFS so we dont have to got through the same paths again.

