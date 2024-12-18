
import graphviz # type: ignore

# Load the dot content from the file (assumed to be generated by Java)
with open("nfa.dot", "r") as file:
    dot_code = file.read()

# Create a Graphviz object and render the graph
graph = graphviz.Source(dot_code)
graph.render("nfa_visualization", format="png", cleanup=True)  # Output PNG
graph.view()  # Automatically open the image
