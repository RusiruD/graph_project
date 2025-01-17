package rusiru.project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AppState {
  public static int numNodes = 0;
  public static int[][] adjacencyMatrix;

  public static Circle previousCircle;
  public static Node previousNode;
  public static boolean alreadyClicked = false;
  public static StackPane previousStackPane;
  public static boolean undirected;
  public static boolean weighted;
  public static boolean isReflexive;
  public static boolean isSymmetric;
  public static boolean isTransitive;
  public static boolean isAntiSymmetric;
  public static boolean isConnected;
  public static boolean isDirectedPseudoGraph;
  public static boolean isDirectedSimpleGraph;
  public static boolean isDirectedMultiGraph;
  public static boolean isPseudoGraph;
  public static boolean isSimpleGraph;
  public static boolean isMultiGraph;
  public static Color nodeColour = Color.BLACK;
  public static Color nodeBorderColour = Color.BLACK;
  public static Color nodeNumColour = Color.WHITE;
  public static Color edgeColour = Color.BLUE;

  public static Color arrowColour = Color.BLACK;
  public static Color arrowBorderColour = Color.YELLOW;
  public static Color weightValueColour = Color.BLACK;
  public static Double edgeThickness = 2.0;
  public static Double arrowSize = 12.0;
  public static Double arrowBorderSize = 1.0;
  public static Double nodeBorderSize = 1.0;
  public static Double nodeNumSize = 12.0;
  public static Double nodeSize = 50.0;
  public static Double weightValueSize = 12.0;

  public static void createAdjacencyMatrix(int numNodes) {
    adjacencyMatrix = new int[numNodes][numNodes];
  }

  public static int[][] getAdjacencyMatrix() {

    return adjacencyMatrix;
  }

  public static void setAdjacencyMatrix(int[][] adjacencyMatrix) {
    for (int i = 0; i < AppState.adjacencyMatrix.length; i++) {
      for (int j = 0; j < AppState.adjacencyMatrix[j].length; j++) {
        adjacencyMatrix[i][j] = AppState.adjacencyMatrix[i][j];
      }
    }

    AppState.adjacencyMatrix = adjacencyMatrix;
  }

  public static void printAdjacencyMatrix() {
    for (int i = 0; i < adjacencyMatrix.length; i++) {
      for (int j = 0; j < adjacencyMatrix[i].length; j++) {
        System.out.print(adjacencyMatrix[i][j] + " ");
      }
      System.out.println(); // Move to the next line after each row
    }
  }

  public static void resetValues() {
    numNodes = 0;
    previousCircle = null;
    previousNode = null;
    alreadyClicked = false;
    previousStackPane = null;
    undirected = false;
    weighted = false;
    isReflexive = false;
    isSymmetric = false;
    isTransitive = false;
    isAntiSymmetric = false;
    isConnected = false;
    isDirectedPseudoGraph = false;
    isDirectedSimpleGraph = false;
    isDirectedMultiGraph = false;
    isPseudoGraph = false;
    isSimpleGraph = false;
    isMultiGraph = false;
    adjacencyMatrix = null;
  }
}
