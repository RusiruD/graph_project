package rusiru.project;

public class Edge {
    private Node Source;
    private Node Destination;
    private int weight;

    public Edge(Node Source, Node Destination, int weight){
        this.Source = Source;
        this.Destination = Destination;
        this.weight = weight;
    }

    public Node getSource() {
        return Source;
    }

    public Node getDestinationNode() {
        return Destination;
    }
    
}
