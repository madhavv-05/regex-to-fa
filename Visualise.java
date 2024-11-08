package Final_FA;

import java.io.*;
import java.util.*;

public class Visualise {
public static void Dot(NFA nfa, String infixExpression) {
    	
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(nfa.startState);
        visited.add(nfa.startState);
        String title="NFA for RE: ";
       

        StringBuilder dot = new StringBuilder();
        dot.append("digraph NFA {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [shape=circle];\n");
        dot.append("  start [shape=point];\n");
        dot.append("  start -> ").append(nfa.startState.id).append(";\n");
        dot.append("  ").append(nfa.acceptState.id).append(" [shape=doublecircle];\n");
        dot.append("  label=\"").append(title).append(infixExpression).append("\";\n\n");
        dot.append("  fontsize=15;\n"); 
        dot.append("  labelloc=top;\n"); 
        dot.append("  fontcolor=\"black\";\n");

        while (!queue.isEmpty()) {
            State state = queue.poll();
            for (Map.Entry<Character, List<State>> entry : state.transitions.entrySet()) {
                char symbol = entry.getKey();
                for (State nextState : entry.getValue()) {
                    dot.append("  ").append(state.id).append(" -> ").append(nextState.id)
                        .append(" [label=\"").append(symbol == 'E' ? "&#949;" : symbol).append("\"];\n");

                    if (!visited.contains(nextState)) {
                        visited.add(nextState);
                        queue.add(nextState);
                    }
                }
            }
        }
        dot.append("}\n");

        try (PrintWriter out = new PrintWriter("nfa.dot")) {
            out.println(dot.toString()); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    private static void run() {
        try {
          
            Process process = Runtime.getRuntime().exec("python visualize_nfa.py");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);  
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println("Error: " + s);  
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        REToNFA converter = new REToNFA();


        System.out.println("Enter a regular expression (infix): ");
        String infix = scanner.nextLine();

   
        String postfix = converter.infixToPostfix(infix);
        System.out.println("Postfix Expression: " + postfix);

       
        NFA nfa = converter.convertToNFA(postfix);

        Dot(nfa,infix);

   
        run();
    }
}
