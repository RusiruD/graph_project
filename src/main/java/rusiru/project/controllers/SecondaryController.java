package rusiru.project.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import rusiru.project.App;
import rusiru.project.AppState;
import rusiru.project.Edge;
import rusiru.project.Node;

public class SecondaryController {
    public static ArrayList<Edge> edges= new ArrayList<Edge>();
    private ArrayList<Node> nodes;

     @FXML Pane root;

     @FXML
    private void initialize(){
        System.out.println("ed");
        root.setPadding(new Insets(20));
         

        // Generate a random integer between 100 and 300 (inclusive)
       
       
        for(int i=0; i<AppState.numNodes; i++){
            createNode(i);
            System.out.println("ed");
              
        }
        AppState.createAdjacencyMatrix(AppState.numNodes);
       
 
      
    }
    @FXML
    private StackPane createNode(int x) {
        Node node = new Node(x,root);
         StackPane dotPane = node.getStackPane();
         return dotPane;
      
       
      
      
    
    }

    @FXML
    private void onPaneClicked(MouseEvent event){
        double clickedX = event.getX();
        double clickedY = event.getY();

        // Check if the clicked coordinates are not within any of the StackPanes
        boolean clickedOnBackground = root.getChildren().stream()
                .filter(node -> node instanceof StackPane)
                .noneMatch(stackPane -> stackPane.getBoundsInParent().contains(clickedX, clickedY));

        if (clickedOnBackground) {
          
        System.out.println("Pane clicked");
        AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");
        AppState.previousStackPane = null;
        AppState.alreadyClicked = false;}
        else{}
    }

    @FXML
    public static boolean isReflexive(){
       int reflexiveEdgeCounter=0;
        for(Edge x:edges){
            if(x.getSource().getNodeNum()==x.getDestinationNode().getNodeNum()){
                reflexiveEdgeCounter++;}}
                if(reflexiveEdgeCounter==AppState.numNodes){
                    return true;
                }
          
         
       
        return false;
    }


    @FXML
    public static boolean isSymmetric(){
        int symmetricEdgeCounter = 0;
        for(Edge edge:edges){
            for(Edge edge1:edges){

                if ((edge.getDestinationNode().getNodeNum())==(edge1.getSource().getNodeNum())){
                    symmetricEdgeCounter++;
                }
            }}
            if (symmetricEdgeCounter == edges.size()) {
                return true;
              } 
              
                return false;
              }

  
    
              public static boolean isTransitive() {
                int possibleTransitiveEdgeCounter = 0;
                int transitiveEdgeCounter = 0;
                // checks if there is an edge with a destination that is the same as the source of another
                // edge-edge1
                for (Edge edge : edges) {
                  // increments the counter if the destination of the first edge is the same as the source of
                  // the second edge
                  // and if the destination of the second edge is not the same as the source of the second edge
                  for (Edge edge1 : edges) {
            
                    if (edge.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum())
                        && !(edge1.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum()))) {
                      possibleTransitiveEdgeCounter++;
                      // checks if there is an edge-edge3 that comes from the same source the first edge did
                      // and goes to the same destination the second edge did
                      for (Edge edge3 : edges) {
                        if (edge.getSource().getNodeNum()==(edge3.getSource().getNodeNum())
                            && edge1.getDestinationNode().getNodeNum()==(edge3.getDestinationNode().getNodeNum())) {
                          transitiveEdgeCounter++;
                        }
                      }
                    }
                  }
                }
                // if the counter is equal to the number of edges, then the graph is transitive
                if (possibleTransitiveEdgeCounter == transitiveEdgeCounter) {
                  return true;
                } else {
                  return false;
                }
              }
            
              /**
               * Checks whether the graph is anti-symmetric.
               *
               * @return true if the graph is anti-symmetric, false otherwise.
               */
              public static boolean isAntiSymmetric() {
                int antisymmetricEdgeCounter = 0;
                int possibleAntiSymmetricEdgeCounter = 0;
                // checks if there is an edge with a destination that is the same as the source of another
                // edge-edge1
                for (Edge edge : edges) {
            
                  for (Edge edge1 : edges) {
                    // increments the counter if the destination of the first edge is the same as the source of
                    // the second edge
                    if (edge.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum())
                        && edge.getSource().getNodeNum()==(edge1.getDestinationNode().getNodeNum())) {
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
                  return true;
                } else {
            
                  return false;
                }
              }
   
       


    @FXML 
    public static boolean isEquivalence(int[][] adjacencyMatrix){
        if(isReflexive() && isSymmetric() && isTransitive()){
            return true;
        }
        return false;}

    @FXML
    public static boolean isPartialOrder(int[][] adjacencyMatrix){
        if(isReflexive() && isAntiSymmetric() && isTransitive()){
            return true;
        }
        return false;
    }

    @FXML
    public static boolean isComplete(){
        int completeEdgeCounter = 0;
        for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum())) {
              completeEdgeCounter++;
            }
          }
        }
        if (completeEdgeCounter == edges.size() * edges.size()) {
          return true;
        } 
          return false;
        
      }

      @FXML
      public static boolean isSimple(){
        
        for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum())) {
              return false;
            }
          }
        }
        for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getSource().getNodeNum()==(edge1.getSource().getNodeNum())
                && edge.getDestinationNode().getNodeNum()==(edge1.getDestinationNode().getNodeNum())
                && edge != edge1) {
              return false;
            }
          }
        }
        return true;
      }
    

}