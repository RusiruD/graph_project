package rusiru.project;

import javafx.scene.layout.StackPane;
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

  public static void createAdjacencyMatrix(int numNodes) {
    adjacencyMatrix = new int[numNodes][numNodes];
  }

  public static int[][] getAdjacencyMatrix() {

    return adjacencyMatrix;
  }

  public static void setAdjacencyMatrix(int[][] adjacencyMatrix) {
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
}
