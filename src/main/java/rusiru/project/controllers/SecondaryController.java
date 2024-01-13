package rusiru.project.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import rusiru.project.AppState;
import rusiru.project.Edge;
import rusiru.project.Node;

public class SecondaryController {
  public static ArrayList<Edge> edges = new ArrayList<Edge>();
  public static ArrayList<Node> nodes = new ArrayList<Node>();
  public static List<List<Integer>> adjacencyList = new ArrayList<List<Integer>>();

  @FXML Pane root;
  @FXML Label reflexiveLbl;
  @FXML Label symmetricLbl;
  @FXML Label transitiveLbl;
  @FXML Label antiSymmetricLbl;
  @FXML Label equivalenceLbl;
  @FXML Label partialOrderLbl;
  @FXML Label simpleGraphLbl;
  @FXML Label multiGraphLbl;
  @FXML Label pseudoGraphLbl;
  @FXML Label completeGraphLbl;
  @FXML ImageView informationImg;
  @FXML Label connectedLbl;
  @FXML Label eulerCircuitLbl;
  @FXML Label acyclicLbl;
  @FXML Label completeDigraphLbl;
  @FXML Label bipartiteLbl;
  @FXML Label treeLbl;
  @FXML Label directedSimpleGraphLbl;
  @FXML Label directedMultiGraphLbl;
  @FXML Label directedPseudoGraphLbl;
  @FXML Button addNodeBtn;
  @FXML Pane checkPropertiesPane;
  @FXML TextArea definitionTextArea;
  @FXML Button submitBtn;
  @FXML TextField numNodesTextField;
  @FXML CheckBox undirected;
  @FXML CheckBox weighted;
  @FXML Pane introPane;
  int numNodesInt;
  @FXML Button resetBtn;
  @FXML Pane titlePane;
  @FXML Text titleTxt;

  @FXML
  private void initialize() {
    // root.setPadding(new Insets(20));
    checkPropertiesPane.layoutXProperty().bind(root.widthProperty().subtract(260));
    // Generate a random integer between 100 and 300 (inclusive)
    titlePane
        .layoutXProperty()
        .bind(root.widthProperty().subtract(titlePane.widthProperty()).divide(2));
  }

  @FXML
  private void onSubmitClicked() throws IOException, NumberFormatException {

    AppState.undirected = undirected.isSelected();
    AppState.weighted = weighted.isSelected();
    System.out.println("Submit clicked");

    if (numNodesTextField.getText().equals("")) {

    } else {

      numNodesInt = Integer.parseInt(numNodesTextField.getText());
      AppState.numNodes = numNodesInt;
      for (int i = 0; i < AppState.numNodes; i++) {
        createNode(i);
        adjacencyList.add(new ArrayList<Integer>());
      }
    }
    submitBtn.setDisable(true);
    undirected.setDisable(true);
    weighted.setDisable(true);
    numNodesTextField.setDisable(true);
    resetBtn.setDisable(false);
    resetBtn.setVisible(true);
    addNodeBtn.setDisable(false);
    addNodeBtn.setVisible(true);
    submitBtn.toBack();
    resetBtn.toFront();
    submitBtn.setVisible(false);
    submitBtn.setDisable(true);
  }

  @FXML
  private StackPane createNode(int x) {
    Node node = new Node(x, root);
    StackPane dotPane = node.getStackPane();
    return dotPane;
  }

  @FXML
  private void onResetClicked(MouseEvent event) {
    System.out.println("Reset clicked");

    root.getChildren().removeIf(node -> node instanceof StackPane);
    root.getChildren().removeIf(node -> node instanceof Line);
    root.getChildren().removeIf(node -> node instanceof QuadCurve);

    root.getChildren().removeIf(node -> node.getId() == "weightTextField");
    root.getChildren().removeIf(node -> node.getId() == "weightLabel");
    AppState.resetValues();
    edges.clear();
    nodes.clear();

    adjacencyList.clear();
    submitBtn.setDisable(false);
    undirected.setDisable(false);
    undirected.setSelected(false);
    weighted.setDisable(false);
    weighted.setSelected(false);
    numNodesTextField.setDisable(false);
    numNodesTextField.setText("");
    resetBtn.setDisable(true);
    resetBtn.setVisible(false);
    resetBtn.toBack();
    submitBtn.toFront();
    submitBtn.setVisible(true);
    submitBtn.setDisable(false);
    resetCheckedProperties();
    addNodeBtn.setDisable(true);
    addNodeBtn.setVisible(false);
  }

  @FXML
  private void resetCheckedProperties() {
    reflexiveLbl.setTextFill(Color.BLACK);
    symmetricLbl.setTextFill(Color.BLACK);
    transitiveLbl.setTextFill(Color.BLACK);
    antiSymmetricLbl.setTextFill(Color.BLACK);
    equivalenceLbl.setTextFill(Color.BLACK);
    partialOrderLbl.setTextFill(Color.BLACK);
    simpleGraphLbl.setTextFill(Color.BLACK);
    multiGraphLbl.setTextFill(Color.BLACK);
    pseudoGraphLbl.setTextFill(Color.BLACK);
    completeGraphLbl.setTextFill(Color.BLACK);
    connectedLbl.setTextFill(Color.BLACK);
    eulerCircuitLbl.setTextFill(Color.BLACK);
    acyclicLbl.setTextFill(Color.BLACK);
    completeDigraphLbl.setTextFill(Color.BLACK);
    bipartiteLbl.setTextFill(Color.BLACK);
    treeLbl.setTextFill(Color.BLACK);
    directedSimpleGraphLbl.setTextFill(Color.BLACK);
    directedMultiGraphLbl.setTextFill(Color.BLACK);
    directedPseudoGraphLbl.setTextFill(Color.BLACK);
  }

  @FXML
  private void onPaneClicked(MouseEvent event) {
    double clickedX = event.getX();
    double clickedY = event.getY();

    // Check if the clicked coordinates are not within any of the StackPanes
    boolean clickedOnBackground =
        root.getChildren().stream()
            .filter(node -> node instanceof StackPane)
            .noneMatch(stackPane -> stackPane.getBoundsInParent().contains(clickedX, clickedY));

    if (clickedOnBackground) {

      if (AppState.previousStackPane != null) {
        AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");
      }
      AppState.previousStackPane = null;
      AppState.alreadyClicked = false;
    } else {
    }
  }

  @FXML
  public void onCheckGraphClicked() {

    System.out.println("Check graph clicked ");

    isReflexive();
    isSymmetric();
    isTransitive();
    isAntiSymmetric();

    isEquivalence();
    isPartialOrder();

    isSimple();
    isMulti();
    isPseudo();

    isComplete();
    isConnected();
    isEulerCircuit();
    isCyclic();
    isBipartite();
    isTree();
  }

  @FXML
  public boolean isReflexive() {

    int reflexiveEdgeCounter = 0;
    for (Edge edge : edges) {

      if (edge.getSource().getNodeNum() == edge.getDestinationNode().getNodeNum()) {

        reflexiveEdgeCounter++;
      }
    }

    if (reflexiveEdgeCounter == AppState.numNodes) {
      reflexiveLbl.setTextFill(Color.GREEN);
      reflexiveLbl.setText(reflexiveLbl.getText() + " " + "\u2713");
      AppState.isReflexive = true;
      return true;
    }

    reflexiveLbl.setTextFill(Color.RED);
    AppState.isReflexive = false;
    return false;
  }

  @FXML
  public boolean isSymmetric() {

    if (AppState.undirected) {
      symmetricLbl.setTextFill(Color.GREEN);
      AppState.isSymmetric = true;
      return true;
    }

    int symmetricEdgeCounter = 0;
    for (Edge edge1 : edges) {
      for (Edge edge2 : edges) {

        if (((edge1.getDestinationNode().getNodeNum()) == (edge2.getSource().getNodeNum()))
            && ((edge1.getSource().getNodeNum()) == (edge2.getDestinationNode().getNodeNum()))) {
          symmetricEdgeCounter++;
        }
      }
    }
    if (symmetricEdgeCounter == edges.size()) {
      symmetricLbl.setTextFill(Color.GREEN);
      AppState.isSymmetric = true;
      return true;
    } else {
      symmetricLbl.setTextFill(Color.RED);
      AppState.isSymmetric = false;
      return false;
    }
  }

  public boolean isTransitive() {

    int possibleTransitiveEdgeCounter = 0;
    int transitiveEdgeCounter = 0;
    // checks if there is an edge with a destination that is the same as the source of another
    // edge-edge1
    for (Edge edge1 : edges) {
      // increments the counter if the destination of the first edge is the same as the source of
      // the second edge
      // and if the destination of the second edge is not the same as the source of the second edge
      for (Edge edge2 : edges) {

        if (edge1.getDestinationNode().getNodeNum() == (edge2.getSource().getNodeNum())
            && (edge1.getDestinationNode().getNodeNum() != (edge1.getSource().getNodeNum())
                && edge2 != edge1
                && edge1.getDestinationNode() != edge1.getSource()
                && edge2.getDestinationNode() != edge2.getSource())) {
          possibleTransitiveEdgeCounter++;

          // checks if there is an edge-edge3 that comes from the same source the first edge did
          // and goes to the same destination the second edge did
          for (Edge edge3 : edges) {
            if (edge1.getSource().getNodeNum() == (edge3.getSource().getNodeNum())
                && edge1.getDestinationNode().getNodeNum()
                    == (edge3.getDestinationNode().getNodeNum())
                && edge1 != edge3
                && edge1 != edge3) {
              transitiveEdgeCounter++;
            }
          }
        }
      }
    }
    // if the counter is equal to the number of edges, then the graph is transitive
    if (possibleTransitiveEdgeCounter == transitiveEdgeCounter) {
      transitiveLbl.setTextFill(Color.GREEN);
      AppState.isTransitive = true;
      return true;
    } else {
      transitiveLbl.setTextFill(Color.RED);
      AppState.isTransitive = false;
      return false;
    }
  }

  public boolean isAntiSymmetric() {
    if (AppState.undirected) {
      antiSymmetricLbl.setTextFill(Color.RED);
      AppState.isAntiSymmetric = false;
      return false;
    }
    int antisymmetricEdgeCounter = 0;
    int possibleAntiSymmetricEdgeCounter = 0;
    // checks if there is an edge with a destination that is the same as the source of another
    // edge-edge1
    for (Edge edge : edges) {

      for (Edge edge1 : edges) {
        // increments the counter if the destination of the first edge is the same as the source of
        // the second edge
        if (edge.getDestinationNode().getNodeNum() == (edge1.getSource().getNodeNum())
            && edge.getSource().getNodeNum() == (edge1.getDestinationNode().getNodeNum())) {
          possibleAntiSymmetricEdgeCounter++;
          // increments different counter if the edges equal each other
          if (edge.equals(edge1)) {
            antisymmetricEdgeCounter++;
          }
        }
      }
    }
    // if the counter is equal to the number of edges, then the graph is anti-symmetric
    if (possibleAntiSymmetricEdgeCounter == antisymmetricEdgeCounter) {
      antiSymmetricLbl.setTextFill(Color.GREEN);
      AppState.isAntiSymmetric = true;
      return true;
    } else {
      antiSymmetricLbl.setTextFill(Color.RED);
      AppState.isAntiSymmetric = false;

      return false;
    }
  }

  @FXML
  public boolean isEquivalence() {
    if (AppState.isReflexive && AppState.isSymmetric && AppState.isTransitive) {
      equivalenceLbl.setTextFill(Color.GREEN);
      return true;
    }
    equivalenceLbl.setTextFill(Color.RED);
    return false;
  }

  @FXML
  public boolean isPartialOrder() {
    if (AppState.isReflexive && AppState.isAntiSymmetric && AppState.isTransitive) {
      partialOrderLbl.setTextFill(Color.GREEN);
      return true;
    }
    partialOrderLbl.setTextFill(Color.RED);
    return false;
  }

  @FXML
  public boolean isSimple() {
    if (!containsMultiEdges() && !containsSelfLoop()) {
      if (AppState.undirected) {
        AppState.isSimpleGraph = true;
        AppState.isDirectedSimpleGraph = false;
        directedSimpleGraphLbl.setTextFill(Color.RED);
        simpleGraphLbl.setTextFill(Color.GREEN);

      } else {
        AppState.isSimpleGraph = false;
        AppState.isDirectedSimpleGraph = true;
        simpleGraphLbl.setTextFill(Color.RED);
        directedSimpleGraphLbl.setTextFill(Color.GREEN);
      }
      return true;
    } else {
      AppState.isSimpleGraph = false;
      AppState.isDirectedSimpleGraph = false;
      simpleGraphLbl.setTextFill(Color.RED);
      directedSimpleGraphLbl.setTextFill(Color.RED);
      return false;
    }
  }

  @FXML
  public boolean isMulti() {

    if (containsMultiEdges() && !containsSelfLoop()) {
      if (AppState.undirected) {
        directedMultiGraphLbl.setTextFill(Color.RED);
        multiGraphLbl.setTextFill(Color.GREEN);
      } else {
        multiGraphLbl.setTextFill(Color.GREEN);
        directedMultiGraphLbl.setTextFill(Color.GREEN);
      }

      return true;

    } else {
      multiGraphLbl.setTextFill(Color.RED);
      directedMultiGraphLbl.setTextFill(Color.RED);
      return false;
    }
  }

  @FXML
  public boolean isPseudo() {

    if (containsMultiEdges() && containsSelfLoop()) {
      if (AppState.undirected) {
        AppState.isPseudoGraph = true;
        AppState.isDirectedPseudoGraph = false;
        directedPseudoGraphLbl.setTextFill(Color.RED);
        pseudoGraphLbl.setTextFill(Color.GREEN);

      } else {
        AppState.isPseudoGraph = false;
        AppState.isDirectedPseudoGraph = true;
        pseudoGraphLbl.setTextFill(Color.RED);
        directedPseudoGraphLbl.setTextFill(Color.GREEN);
      }
      return true;
    } else {
      AppState.isPseudoGraph = false;
      AppState.isDirectedPseudoGraph = false;
      pseudoGraphLbl.setTextFill(Color.RED);
      directedPseudoGraphLbl.setTextFill(Color.RED);
      return false;
    }
  }

  @FXML
  public boolean isComplete() {
    completeDigraphLbl.setTextFill(Color.RED);
    completeGraphLbl.setTextFill(Color.RED);
    double numEdges = 0.5 * AppState.numNodes * (AppState.numNodes - 1);
    if (AppState.undirected & (!AppState.isSimpleGraph || edges.size() != 2 * (int) numEdges)) {
      completeGraphLbl.setTextFill(Color.RED);
      return false;
    } else if (!AppState.undirected & edges.size() != 2 * numEdges) {
      completeDigraphLbl.setTextFill(Color.RED);
      return false;
    }

    for (Node node : nodes) {
      if ((node.getInDegree()) != AppState.numNodes - 1) {
        completeGraphLbl.setTextFill(Color.RED);
        completeDigraphLbl.setTextFill(Color.RED);
        return false;
      }
    }

    if (AppState.undirected) {
      completeGraphLbl.setTextFill(Color.GREEN);
      return true;
    } else {
      completeDigraphLbl.setTextFill(Color.GREEN);
      return true;
    }
  }

  @FXML
  public boolean isConnected() {
    Set<Integer> visited = new HashSet<>();

    // Choose any vertex as the starting point
    int startVertex = 0;

    // Perform DFS from the starting vertex
    dfs(edges, startVertex, visited);

    // Check if all vertices are visited
    if (visited.size() == AppState.numNodes) {
      connectedLbl.setTextFill(Color.GREEN);
      AppState.isConnected = true;
      return true;
    }
    connectedLbl.setTextFill(Color.RED);
    AppState.isConnected = false;
    return false;
  }

  public boolean isEulerCircuit() {

    if (AppState.isConnected) {
      for (Node node : nodes) {

        if (AppState.undirected) {
          if ((node.getInDegree() + node.getOutDegree()) % 2 != 0) {

            eulerCircuitLbl.setTextFill(Color.RED);
            return false;
          }
        } else {
          if ((node.getInDegree() != node.getOutDegree())) {

            eulerCircuitLbl.setTextFill(Color.RED);
            return false;
          }
        }
      }

      eulerCircuitLbl.setTextFill(Color.GREEN);
      return true;
    }
    eulerCircuitLbl.setTextFill(Color.RED);

    return false;
  }

  public boolean isBipartite() {
    boolean isUndirected = AppState.undirected;
    boolean isDirected = !isUndirected; // Assuming undirected or directed, not both
    int numNodes = AppState.numNodes;
    int[] colors = new int[numNodes + 1];
    Arrays.fill(colors, -1);

    for (Node node : nodes) {
      if (colors[node.getNodeNum()] == -1) {
        if (isDirected) {
          if (!isBipartiteDFS(node.getNodeNum(), colors)) {
            bipartiteLbl.setTextFill(Color.RED);
            return false;
          }
        } else {
          if (!bipartiteBfs(node.getNodeNum(), edges, colors)) {
            bipartiteLbl.setTextFill(Color.RED);
            return false;
          }
        }
      }
    }

    bipartiteLbl.setTextFill(Color.GREEN);
    return true;
  }

  private boolean isBipartiteDFS(int vertex, int[] colors) {
    Stack<Integer> stack = new Stack<>();
    stack.push(vertex);
    colors[vertex] = 0; // Assign color 0 to the starting vertex

    while (!stack.isEmpty()) {
      int current = stack.pop();

      for (int neighbor : adjacencyList.get(current)) {
        if (colors[neighbor] == -1) {
          colors[neighbor] = 1 - colors[current]; // Assign the opposite color
          stack.push(neighbor);
        } else if (colors[neighbor] == colors[current]) {
          // If adjacent vertices have the same color, the graph is not bipartite
          return false;
        }
      }
    }

    return true;
  }

  private static boolean bipartiteBfs(int startNode, ArrayList<Edge> edges, int[] colors) {

    Queue<Integer> queue = new LinkedList<>();
    queue.add(startNode);
    colors[startNode] = 0;

    while (!queue.isEmpty()) {
      int current = queue.poll();

      for (Edge edge : edges) {
        int neighbor;
        if (!AppState.undirected) {
          if (edge.getSource().getNodeNum() == current) {
            neighbor = edge.getDestinationNode().getNodeNum();
          } else {
            continue; // Skip edges that don't originate from the current node in a directed graph
          }
        } else {
          // For undirected graph, consider both source and destination
          if (edge.getSource().getNodeNum() == current) {
            neighbor = edge.getDestinationNode().getNodeNum();
          } else if (edge.getDestinationNode().getNodeNum() == current) {
            neighbor = edge.getSource().getNodeNum();
          } else {
            continue; // Skip edges that don't involve the current node in an undirected graph
          }
        }

        if (colors[neighbor] == -1) {
          colors[neighbor] = 1 - colors[current];
          queue.add(neighbor);
        } else if (colors[neighbor] == colors[current]) {
          return false; // Graph is not bipartite
        }
      }
    }

    return true; // Graph is bipartite
  }

  // Add this method to your SecondaryController class

  public boolean isCyclic() {
    Set<Integer> visited = new HashSet<>();
    Set<Integer> currentlyVisiting = new HashSet<>();

    for (Node node : nodes) {
      int nodeNum = node.getNodeNum();
      if (!visited.contains(nodeNum)
          && isCyclicDFS(nodeNum, -1, visited, currentlyVisiting, 3, AppState.undirected)) {
        acyclicLbl.setTextFill(Color.RED);
        return true;
      }
    }
    acyclicLbl.setTextFill(Color.GREEN);
    return false;
  }

  private boolean isCyclicDFS(
      int current,
      int parent,
      Set<Integer> visited,
      Set<Integer> currentlyVisiting,
      int minCycleSize,
      boolean isUndirected) {
    visited.add(current);
    currentlyVisiting.add(current);

    for (Edge edge : edges) {
      int neighbor;
      if (!isUndirected) {
        if (edge.getSource().getNodeNum() == current) {
          neighbor = edge.getDestinationNode().getNodeNum();
        } else {
          continue; // Skip edges that don't originate from the current node in a directed graph
        }
      } else {
        // For undirected graph, consider both source and destination
        if (edge.getSource().getNodeNum() == current) {
          neighbor = edge.getDestinationNode().getNodeNum();
        } else if (edge.getDestinationNode().getNodeNum() == current) {
          neighbor = edge.getSource().getNodeNum();
        } else {
          continue; // Skip edges that don't involve the current node in an undirected graph
        }
      }

      if (!visited.contains(neighbor)) {
        if (isCyclicDFS(
            neighbor, current, visited, currentlyVisiting, minCycleSize, AppState.undirected)) {
          return true;
        }
      } else if (currentlyVisiting.contains(neighbor)
          && currentlyVisiting.size() >= minCycleSize
          && neighbor != parent) {
        // If the neighbor is in the currentlyVisiting set, the size is >= minCycleSize, and it's
        // not the parent, there is a cycle
        return true;
      }
    }

    currentlyVisiting.remove(current); // Backtrack when leaving the current node
    return false;
  }

  public boolean isTree() {
    if (isConnected() && !isCyclic() && AppState.undirected) {
      treeLbl.setTextFill(Color.GREEN);
      return true;
    }
    treeLbl.setTextFill(Color.RED);
    return false;
  }

  private static void dfs(List<Edge> edges, int vertex, Set<Integer> visited) {
    if (visited.contains(vertex)) {
      return;
    }

    visited.add(vertex);

    for (Edge edge : edges) {
      if (edge.getSource().getNodeNum() == vertex) {
        dfs(edges, edge.getDestinationNode().getNodeNum(), visited);
      }
      if (AppState.undirected) {

        if (edge.getDestinationNode().getNodeNum() == vertex) {
          dfs(edges, edge.getSource().getNodeNum(), visited);
        }
      }
    }
  }

  public boolean containsSelfLoop() {
    for (Edge edge : edges) {
      if (edge.getSource().getNodeNum() == edge.getDestinationNode().getNodeNum()) {
        return true;
      }
    }
    return false;
  }

  public boolean containsMultiEdges() {
    for (Edge edge1 : edges) {
      for (Edge edge2 : edges) {
        if (edge1.getSource().getNodeNum() == edge2.getSource().getNodeNum()
            && edge1.getDestinationNode().getNodeNum() == edge2.getDestinationNode().getNodeNum()
            && edge1 != edge2) {
          return true;
        }
      }
    }
    return false;
  }

  @FXML
  private void addNode() {
    AppState.numNodes++;
    createNode(AppState.numNodes - 1);
    adjacencyList.add(new ArrayList<Integer>());
  }

  @FXML
  private void revealDefinitions() {
    if (definitionTextArea.isVisible()) {
      definitionTextArea.setVisible(false);
    } else {
      definitionTextArea.setVisible(true);
      definitionTextArea.toFront();
    }
  }
}
