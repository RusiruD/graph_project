package rusiru.project.controllers;

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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import rusiru.project.AppState;
import rusiru.project.Edge;
import rusiru.project.Node;

public class SecondaryController {
  public static ArrayList<Edge> edges = new ArrayList<Edge>();
  public static ArrayList<Node> nodes = new ArrayList<Node>();
  public static ArrayList<Line> lines = new ArrayList<Line>();
  public static ArrayList<QuadCurve> arcs = new ArrayList<QuadCurve>();
  public static ArrayList<StackPane> arrows = new ArrayList<StackPane>();
  public static List<List<Integer>> adjacencyList = new ArrayList<List<Integer>>();
  public static ArrayList<Label> graphPropertyLabels = new ArrayList<Label>();
  public static ArrayList<Label> weightLabels = new ArrayList<Label>();

  @FXML Pane root;
  @FXML Pane settingsPane;
  @FXML Button settingsBtn;
  @FXML Text warningTxt;
  @FXML Text warningTxt2;
  @FXML Label reflexiveLbl;
  @FXML Button checkGraph;
  @FXML Label symmetricLbl;
  @FXML Label transitiveLbl;
  @FXML Label antiSymmetricLbl;
  @FXML Label equivalenceLbl;
  @FXML Label partialOrderLbl;
  @FXML Label simpleGraphLbl;
  @FXML Label multiGraphLbl;
  @FXML Label pseudoGraphLbl;
  @FXML Label completeGraphLbl;
  @FXML Button informationBtn;
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
  @FXML ColorPicker nodeColPicker;
  @FXML ColorPicker edgeColPicker;
  @FXML ColorPicker arrowColPicker;
  @FXML ColorPicker nodeBorderColPicker;
  @FXML ColorPicker arrowBorderColPicker;
  @FXML ColorPicker nodeNumColPicker;
  @FXML ColorPicker weightValueColPicker;
  @FXML TextField edgeThicknessTextField;
  @FXML TextField nodeSizeTextField;
  @FXML TextField nodeBorderSizeTextField;
  @FXML TextField arrowSizeTextField;
  @FXML TextField arrowBorderSizeTextField;
  @FXML TextField nodeNumberSizeTextField;
  @FXML TextField weightValueSizeTextField;
  @FXML Button resetSettingsBtn;
  @FXML Button updateSettingsBtn;
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

    for (TextField textField :
        Arrays.asList(
            edgeThicknessTextField,
            nodeSizeTextField,
            nodeBorderSizeTextField,
            arrowSizeTextField,
            arrowBorderSizeTextField,
            weightValueSizeTextField,
            nodeNumberSizeTextField)) {
      textField.setOnKeyTyped(
          event -> {
            if (isNonZeroNumber(textField.getText())) {
              warningTxt2.setVisible(false);
            } else {
              warningTxt2.setVisible(true);
            }
          });
    }
    numNodesTextField.setOnKeyTyped(
        event -> {
          if (isNonZeroNumber(numNodesTextField.getText())) {
            warningTxt.setVisible(false);

          } else {
            System.out.println("Not a number");
            warningTxt.setVisible(true);
          }
        });
    edgeThicknessTextField.setOnKeyTyped(
        event -> {
          if (isNonZeroNumber(edgeThicknessTextField.getText())) {
            warningTxt2.setVisible(false);
          } else {
            warningTxt2.setVisible(true);
          }
        });
  }

  @FXML
  private void onSubmitClicked() {
    checkGraph.setDisable(false);

    AppState.undirected = undirected.isSelected();
    AppState.weighted = weighted.isSelected();
    System.out.println("Submit clicked");

    if (!isNonZeroNumber(numNodesTextField.getText())) {
      warningTxt.setVisible(true);
      return;
    } else {
      warningTxt.setVisible(false);
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
  private boolean isNonZeroNumber(String text) {

    if (text.matches("\\d+")) {
      try {
        double number = Double.parseDouble(text);
        return number != 0;
      } catch (NumberFormatException e) {
        // If parsing fails, it's not a valid number
        return false;
      }
    } else {
      // If the string contains non-digit characters
      return false;
    }
  }

  @FXML
  private StackPane createNode(int x) {
    Node node = new Node(x, root);
    StackPane dotPane = node.getStackPane();
    return dotPane;
  }

  @FXML
  private void onResetClicked(MouseEvent event) {
    checkGraph.setDisable(true);
    System.out.println("Reset clicked");

    for (javafx.scene.Node child : checkPropertiesPane.getChildren()) {

      if (child instanceof Label) {
        Label label = (Label) child;
        if (label.getText().contains("\u2713")) {
          label.setText(label.getText().substring(0, label.getText().length() - 2));
        } else if (label.getText().contains("\u2717")) {
          label.setText(label.getText().substring(0, label.getText().length() - 2));
        }
      }
    }
    root.getChildren().removeIf(node -> node instanceof StackPane);
    root.getChildren().removeIf(node -> node instanceof Line);
    root.getChildren().removeIf(node -> node instanceof QuadCurve);
    onResetSettingsClicked();
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
        AppState.previousStackPane.setEffect(null);
        AppState.previousStackPane.getChildren().get(0).setScaleX(1);
        AppState.previousStackPane.getChildren().get(0).setScaleY(1);
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
  private void setCorrectLabel(Label label) {
    if (label.getText().contains("\u2717")) {
      label.setText(label.getText().substring(0, label.getText().length() - 2));
    } else if (label.getText().contains("\u2713")) {
      return;
    }
    label.setTextFill(Color.GREEN);
    label.setText(label.getText() + " \u2713");
  }

  @FXML
  private void setIncorrectLabel(Label label) {
    if (label.getText().contains("\u2713")) {
      label.setText(label.getText().substring(0, label.getText().length() - 2));

    } else if (label.getText().contains("\u2717")) {
      return;
    }

    label.setTextFill(Color.RED);
    label.setText(label.getText() + " \u2717");
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

      AppState.isReflexive = true;
      setCorrectLabel(reflexiveLbl);
      return true;
    }

    setIncorrectLabel(reflexiveLbl);

    AppState.isReflexive = false;
    return false;
  }

  @FXML
  public boolean isSymmetric() {

    if (AppState.undirected) {
      setCorrectLabel(symmetricLbl);
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
      setCorrectLabel(symmetricLbl);
      AppState.isSymmetric = true;
      return true;
    } else {
      setIncorrectLabel(symmetricLbl);
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
                && edge2.getDestinationNode() != edge1.getSource()
                && edge2.getDestinationNode() != edge2.getSource())) {

          possibleTransitiveEdgeCounter++;

          // checks if there is an edge-edge3 that comes from the same source the first edge did
          // and goes to the same destination the second edge did
          for (Edge edge3 : edges) {

            if (edge1.getSource().getNodeNum() == (edge3.getSource().getNodeNum())
                && edge2.getDestinationNode().getNodeNum()
                    == (edge3.getDestinationNode().getNodeNum())
                && edge1 != edge3
                && edge2 != edge3) {

              transitiveEdgeCounter++;
            }
          }
        }
      }
    }

    // if the counter is equal to the number of edges, then the graph is transitive
    if (possibleTransitiveEdgeCounter == transitiveEdgeCounter) {
      setCorrectLabel(transitiveLbl);
      AppState.isTransitive = true;
      return true;
    } else {
      setIncorrectLabel(transitiveLbl);
      AppState.isTransitive = false;
      return false;
    }
  }

  public boolean isAntiSymmetric() {
    if (AppState.undirected) {
      setIncorrectLabel(antiSymmetricLbl);
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
      setCorrectLabel(antiSymmetricLbl);
      AppState.isAntiSymmetric = true;
      return true;
    } else {
      setIncorrectLabel(antiSymmetricLbl);
      AppState.isAntiSymmetric = false;

      return false;
    }
  }

  @FXML
  public boolean isEquivalence() {
    if (AppState.isReflexive && AppState.isSymmetric && AppState.isTransitive) {
      setCorrectLabel(equivalenceLbl);
      return true;
    }
    setIncorrectLabel(equivalenceLbl);
    return false;
  }

  @FXML
  public boolean isPartialOrder() {
    if (AppState.isReflexive && AppState.isAntiSymmetric && AppState.isTransitive) {
      setCorrectLabel(partialOrderLbl);
      return true;
    }
    setIncorrectLabel(partialOrderLbl);
    return false;
  }

  @FXML
  public boolean isSimple() {
    if (!containsMultiEdges() && !containsSelfLoop()) {
      if (AppState.undirected) {
        AppState.isSimpleGraph = true;
        AppState.isDirectedSimpleGraph = false;
        setIncorrectLabel(directedSimpleGraphLbl);
        setCorrectLabel(simpleGraphLbl);

      } else {
        AppState.isSimpleGraph = false;
        AppState.isDirectedSimpleGraph = true;
        setCorrectLabel(directedSimpleGraphLbl);
        setIncorrectLabel(simpleGraphLbl);
      }
      return true;
    } else {
      AppState.isSimpleGraph = false;
      AppState.isDirectedSimpleGraph = false;
      setIncorrectLabel(simpleGraphLbl);
      setIncorrectLabel(directedSimpleGraphLbl);

      return false;
    }
  }

  @FXML
  public boolean isMulti() {

    if (containsMultiEdges() && !containsSelfLoop()) {
      if (AppState.undirected) {
        setCorrectLabel(multiGraphLbl);
        setIncorrectLabel(directedMultiGraphLbl);

      } else {
        setCorrectLabel(directedMultiGraphLbl);
        setCorrectLabel(multiGraphLbl);
      }

      return true;

    } else {
      setIncorrectLabel(multiGraphLbl);
      setIncorrectLabel(directedMultiGraphLbl);
      // why both
      return false;
    }
  }

  @FXML
  public boolean isPseudo() {

    if (containsSelfLoop()) {
      if (AppState.undirected) {
        AppState.isPseudoGraph = true;
        AppState.isDirectedPseudoGraph = false;
        setIncorrectLabel(directedPseudoGraphLbl);
        setCorrectLabel(pseudoGraphLbl);

      } else {
        AppState.isPseudoGraph = false;
        AppState.isDirectedPseudoGraph = true;
        setCorrectLabel(directedPseudoGraphLbl);
        setIncorrectLabel(pseudoGraphLbl);
      }
      return true;
    } else {
      AppState.isPseudoGraph = false;
      AppState.isDirectedPseudoGraph = false;
      setIncorrectLabel(pseudoGraphLbl);
      setIncorrectLabel(directedPseudoGraphLbl);

      return false;
    }
  }

  @FXML
  public boolean isComplete() {
    completeDigraphLbl.setTextFill(Color.RED);
    completeGraphLbl.setTextFill(Color.RED);
    double numEdges = 0.5 * AppState.numNodes * (AppState.numNodes - 1);
    if (AppState.undirected & (!AppState.isSimpleGraph || edges.size() != 2 * (int) numEdges)) {
      setIncorrectLabel(completeGraphLbl);

      return false;
    } else if (!AppState.undirected & edges.size() != 2 * numEdges) {
      setIncorrectLabel(completeDigraphLbl);

      return false;
    }

    for (Node node : nodes) {
      if ((node.getInDegree()) != AppState.numNodes - 1) {
        setIncorrectLabel(completeDigraphLbl);
        setIncorrectLabel(completeGraphLbl);

        return false;
      }
    }

    if (AppState.undirected) {
      setCorrectLabel(completeGraphLbl);
      return true;
    } else {
      setCorrectLabel(completeDigraphLbl);
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
      setCorrectLabel(connectedLbl);
      AppState.isConnected = true;
      return true;
    }
    setIncorrectLabel(connectedLbl);
    AppState.isConnected = false;
    return false;
  }

  public boolean isEulerCircuit() {

    if (AppState.isConnected) {
      for (Node node : nodes) {

        if (AppState.undirected) {
          if ((node.getInDegree() + node.getOutDegree()) % 2 != 0) {

            setIncorrectLabel(eulerCircuitLbl);
            return false;
          }
        } else {
          if ((node.getInDegree() != node.getOutDegree())) {

            setIncorrectLabel(eulerCircuitLbl);
            return false;
          }
        }
      }

      setCorrectLabel(eulerCircuitLbl);
      return true;
    }
    setIncorrectLabel(eulerCircuitLbl);

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
            setIncorrectLabel(bipartiteLbl);
            return false;
          }
        } else {
          if (!bipartiteBfs(node.getNodeNum(), edges, colors)) {
            setIncorrectLabel(bipartiteLbl);
            return false;
          }
        }
      }
    }
    setCorrectLabel(bipartiteLbl);
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
        setIncorrectLabel(acyclicLbl);
        return true;
      }
    }
    setCorrectLabel(acyclicLbl);
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
      setCorrectLabel(treeLbl);
      return true;
    }
    setIncorrectLabel(treeLbl);
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
      informationBtn.setStyle(null);
      definitionTextArea.setVisible(false);
    } else {
      definitionTextArea.setVisible(true);
      definitionTextArea.toFront();
      informationBtn.setStyle("  -fx-background-color:rgb(168, 167, 167);");
    }
  }

  @FXML
  private void onSettingsClicked() {
    if (settingsPane.isVisible()) {
      settingsPane.toBack();
      settingsPane.setVisible(false);
      settingsPane.setDisable(true);
      settingsBtn.setStyle(null);
    } else {
      settingsBtn.setStyle("  -fx-background-color:rgb(168, 167, 167);");
      settingsPane.toFront();
      settingsPane.setDisable(false);
      settingsPane.setVisible(true);
    }
  }

  @FXML
  private void updateSettings() {
    if (warningTxt2.isVisible()) {

      return;
    } else {
      AppState.nodeColour = nodeColPicker.getValue();
      AppState.edgeColour = edgeColPicker.getValue();
      AppState.arrowColour = arrowColPicker.getValue();
      AppState.nodeBorderColour = nodeBorderColPicker.getValue();
      AppState.arrowBorderColour = arrowBorderColPicker.getValue();
      AppState.nodeNumColour = nodeNumColPicker.getValue();
      AppState.edgeThickness = Double.parseDouble(edgeThicknessTextField.getText());
      AppState.nodeSize = Double.parseDouble(nodeSizeTextField.getText());
      AppState.nodeBorderSize = Double.parseDouble(nodeBorderSizeTextField.getText());
      AppState.arrowSize = Double.parseDouble(arrowSizeTextField.getText());
      AppState.arrowBorderSize = Double.parseDouble(arrowBorderSizeTextField.getText());
      AppState.nodeNumSize = Double.parseDouble(nodeNumberSizeTextField.getText());
      AppState.weightValueColour = weightValueColPicker.getValue();
      AppState.weightValueSize = Double.parseDouble(weightValueSizeTextField.getText());

      String nodeCol = getColorCode(nodeColPicker.getValue());
      String nodeBorderCol = getColorCode(nodeBorderColPicker.getValue());
      String nodeNumCol = getColorCode(nodeNumColPicker.getValue());
      String arrowCol = getColorCode(arrowColPicker.getValue());
      String arrowBorderCol = getColorCode(arrowBorderColPicker.getValue());
      updateNode(
          nodeCol,
          nodeBorderCol,
          nodeNumCol,
          AppState.nodeSize / 2,
          AppState.nodeBorderSize,
          AppState.nodeNumSize);
      updateArrows(arrowCol, arrowBorderCol, AppState.arrowSize, AppState.arrowBorderSize);
      updateWeightLabels();
      updateEdges(Double.parseDouble(edgeThicknessTextField.getText()));
    }
  }

  @FXML
  private void onResetSettingsClicked() {
    warningTxt2.setVisible(false);
    nodeColPicker.setValue(Color.BLACK);
    edgeColPicker.setValue(Color.BLUE);
    arrowColPicker.setValue(Color.BLACK);
    nodeBorderColPicker.setValue(Color.BLACK);
    arrowBorderColPicker.setValue(Color.YELLOW);
    nodeNumColPicker.setValue(Color.WHITE);
    edgeThicknessTextField.setText("2");
    nodeSizeTextField.setText("50");
    nodeBorderSizeTextField.setText("0");
    arrowSizeTextField.setText("12");
    arrowBorderSizeTextField.setText("1");
    nodeNumberSizeTextField.setText("14");
    weightValueColPicker.setValue(Color.BLACK);
    weightValueSizeTextField.setText("14");

    updateSettings();
  }

  private String getColorCode(Color color) {
    return String.format(
        "#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  @FXML
  private void updateNode(
      String fillColour,
      String borderColour,
      String numColour,
      Double size,
      Double borderSize,
      Double numSize) {
    for (Node node : nodes) {

      Circle circle = (Circle) node.getStackPane().getChildren().get(0);
      circle.radiusProperty().setValue(size);
      node.getStackPane()
          .getChildren()
          .get(0)
          .setStyle(
              "-fx-fill:"
                  + fillColour
                  + ";-fx-stroke:"
                  + borderColour
                  + ";-fx-stroke-width:"
                  + borderSize
                  + ";-fx-radius:"
                  + size
                  + "px;");
      node.getStackPane()
          .getChildren()
          .get(1)
          .setStyle("-fx-text-fill:" + numColour + ";-fx-font-size:" + numSize + "px;");
    }
  }

  @FXML
  private void updateEdges(Double edgeThickness) {
    for (QuadCurve arc : arcs) {
      arc.setStroke(edgeColPicker.getValue());
      arc.setStrokeWidth(edgeThickness);
    }
    for (Line line : lines) {
      line.setStroke(edgeColPicker.getValue());
      line.setStrokeWidth(edgeThickness);
    }
  }

  @FXML
  private void updateArrows(
      String fillColour, String borderColour, Double arrowSize, Double borderSize) {
    for (StackPane arrow : arrows) {
      arrow.setPrefSize(arrowSize, arrowSize);
      arrow.setStyle(
          "-fx-background-color:"
              + fillColour
              + ";-fx-border-width:"
              + borderSize
              + "px;-fx-border-color:"
              + borderColour
              + " ;-fx-shape:"
              + " \"M0,-4L4,0L0,4Z\"");
    }
  }

  @FXML
  private void updateWeightLabels() {
    for (Label label : weightLabels) {
      label.setTextFill(weightValueColPicker.getValue());
      label.setStyle("-fx-font-size:" + weightValueSizeTextField.getText() + "px;");
    }
  }
}
