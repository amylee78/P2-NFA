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
            -  javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java
        3. use this to run the test
            -  java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/hamcrest.jar org.jucd -
            nit.runner.JUnitCore test.dfa.DFATest 
        4. If running into such as having junit5. Please use these commands once you are in the root file to compile it
            - javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar test/dfa/DFATest.java 
        5. then to run it use this command 
            -  java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.dfa.DFATest


once done, the output should have a output like when sucessful:  
nfa1 <test> done



## PROGRAM DESIGN AND IMPORTANT CONCEPTS

 This project models  a instance of an Nondeterministic Finite Automaton (NFA) and its behavior. NFAs are one of the Finite Automata 
 abstract machines used to reconize patterns in a input sequence.  NFA consists of a  5-tuple that represent the states, alphabet, 
 transition  start  and final states. SSimilar to DFA, NFA allows transitions to mutiple states for one 
 input and allows empty string transion: " ε " , represented via "e" in this program.

### Main concepts and organization
   -  NFAState - This class represents a single State in NFA that stores its transitions as Map<Character, Set<NFAState>> transitions  from input symbols( aka the alphabet : a, 1, 0, b etc) to the target states, allowing mutiple transitions for the same symbol. It supports ε-transitions via the character 'e'.

   - NFA - this class manages NFA such as  all states, the alphabet, transitions, start and final states. Some important algorithms in this progam are 
        - eClosure(NFAState s) - computes the set of all NFA states reachable from s (starting state) through ε-transitions using depth-first search (DFS) with a stack. It uses DFS to explore states that are reachable by ε and pushing it into the stack. It adds it to the visited state and pushes any unvisted states into the stack into all reachable states are found.
        - accepts(String s) - 
        - Config and ConfigKey – Helper classes is used to stimulate BFS. Config stores the current active states and input index. ConfigKey is used to track visited configurations to avoid anyredundant processing.



## DISCUSSION
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?
 
 


## SOURCES

