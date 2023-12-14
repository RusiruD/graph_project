package rusiru.project;

public class Edge {
  private Node Source;
  private Node Destination;
  private int weight;

  public Edge(Node Source, Node Destination, int weight) {
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

  public int getWeight() {
    return weight;
  }

  public void setSource(Node Source) {
    this.Source = Source;
  }

  public void setDestination(Node Destination) {
    this.Destination = Destination;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }
}
