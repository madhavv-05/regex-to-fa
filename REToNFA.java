
package Final_FA;

import java.util.*;

class State {
    int id;
    
    Map<Character, List<State>> transitions = new HashMap<>();

    public State(int id) {
        this.id = id;
    }

    public void addTransition(char symbol, State nextState) {
        
    	transitions.putIfAbsent(symbol, new ArrayList<>());
     transitions.get(symbol).add(nextState);
    }
}

class NFA {
    State startState;
    
    State acceptState;

    public NFA(State startState, State acceptState) {
              this.startState = startState;
        this.acceptState = acceptState;
    }
}

public class REToNFA {
    private int stateId = 0;


    private State createState() {
        return new State(stateId++);
    }

 
    public String infixToPostfix(String infix) {
    	
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char ch : infix.toCharArray()) {
            switch (ch) {
                case '(':
                    stack.push(ch);
                    break;
                case ')':
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfix.append(stack.pop());
                    }
                    stack.pop(); 
                    break;
                case '*':
                    postfix.append(ch);
                    break;
                case '|':
                case '.':
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(ch)) {
                        postfix.append(stack.pop());
                    }
                    stack.push(ch);
                    break;
                default: 
                	postfix.append(ch);
                    break;
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private int precedence(char ch) {
        switch (ch) {
            case '*': return 3;
            case '.': return 2;
            case '|': return 1;
            default: return 0;
        }
    }

    public NFA convertToNFA(String postfix) {
        Stack<NFA> stack = new Stack<>();
        for (char symbol : postfix.toCharArray()) {
            switch (symbol) {
                case '*':
                    stack.push(Star(stack.pop()));
                    break;
                case '|':
                    NFA nfa2 = stack.pop();
                    NFA nfa1 = stack.pop();
                    stack.push(Union(nfa1, nfa2));
                    break;
                case '.':
                    NFA nfa2Concat = stack.pop();
                    NFA nfa1Concat = stack.pop();
                    stack.push(Concat(nfa1Concat, nfa2Concat));
                    break;
                default:
                    stack.push(BasicNFA(symbol));
                    break;
            }
        }
        return stack.pop();
    }

    private NFA BasicNFA(char symbol) {
        State start = createState();
      State accept = createState();
         
        start.addTransition(symbol, accept);
        return new NFA(start, accept);
    }

    private NFA Concat(NFA nfa1, NFA nfa2) {
        nfa1.acceptState.addTransition('E', nfa2.startState);
        return new NFA(nfa1.startState, nfa2.acceptState);
    }

    private NFA Union(NFA nfa1, NFA nfa2) {
        State start = createState();
        State accept = createState();
        start.addTransition('E', nfa1.startState);
        start.addTransition('E', nfa2.startState);
        nfa1.acceptState.addTransition('E', accept);
        nfa2.acceptState.addTransition('E', accept);
        return new NFA(start, accept);
    }

    private NFA Star(NFA nfa) {
        State start = createState();
        State accept = createState();
        start.addTransition('E', nfa.startState);
        start.addTransition('E', accept);
        nfa.acceptState.addTransition('E', nfa.startState);
        nfa.acceptState.addTransition('E', accept);
        return new NFA(start, accept);
    }

    

    public static void main(String[] args) {
       
}
}