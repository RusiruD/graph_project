package rusiru.project.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import rusiru.project.App;
import rusiru.project.AppState;
import rusiru.project.Edge;
import rusiru.project.Node;

public class SecondaryController {
    public static ArrayList<Edge> edges= new ArrayList<Edge>();
    public static ArrayList<Node> nodes = new ArrayList<Node>();

     @FXML Pane root;
    @FXML  Label reflexiveLbl;
    @FXML  Label symmetricLbl;
    @FXML  Label transitiveLbl;
    @FXML  Label antiSymmetricLbl;
    @FXML  Label equivalenceLbl;
    @FXML  Label partialOrderLbl;
    @FXML  Label simpleGraphLbl;
    @FXML  Label multiGraphLbl;
    @FXML  Label pseudoGraphLbl;
    @FXML  Label completeGraphLbl;
    @FXML  Label connectedLbl;
    @FXML  Label eulerCircuitLbl;
  
    
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
      public void onCheckGraphClicked(){
        System.out.println("Check graph clicked");
       
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
        
      }

    @FXML
    public  boolean isReflexive(){
       int reflexiveEdgeCounter=0;
        for(Edge x:edges){
            if(x.getSource().getNodeNum()==x.getDestinationNode().getNodeNum()){
                reflexiveEdgeCounter++;}}
                if(reflexiveEdgeCounter==AppState.numNodes){
                    reflexiveLbl.setTextFill(Color.GREEN);
                    return true;
                }
          
               
         reflexiveLbl.toFront();
        reflexiveLbl.setTextFill(Color.RED);
        return false;
    }


    @FXML
    public  boolean isSymmetric(){
        int symmetricEdgeCounter = 0;
        for(Edge edge:edges){
            for(Edge edge1:edges){

                if (((edge.getDestinationNode().getNodeNum())==(edge1.getSource().getNodeNum()))
                && ((edge.getSource().getNodeNum())==(edge1.getDestinationNode().getNodeNum())) && edge != edge1){
                    symmetricEdgeCounter++;
                }
            }}
            if (symmetricEdgeCounter == edges.size()) {
                symmetricLbl.setTextFill(Color.GREEN);
                return true;
              } 
                symmetricLbl.setTextFill(Color.RED);
                return false;
              }

  
    
              public  boolean isTransitive() {
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
                        && (edge1.getDestinationNode().getNodeNum()!=(edge.getSource().getNodeNum()))) {
                      possibleTransitiveEdgeCounter++;
                      // checks if there is an edge-edge3 that comes from the same source the first edge did
                      // and goes to the same destination the second edge did
                      for (Edge edge3 : edges) {
                        if (edge.getSource().getNodeNum()==(edge3.getSource().getNodeNum())
                            && edge1.getDestinationNode().getNodeNum()==(edge3.getDestinationNode().getNodeNum()) && edge != edge3 && edge1 != edge3) {
                          transitiveEdgeCounter++;
                        }
                      }
                    }
                  }
                }
                // if the counter is equal to the number of edges, then the graph is transitive
                if (possibleTransitiveEdgeCounter == transitiveEdgeCounter) {
                    transitiveLbl.setTextFill(Color.GREEN);
                  return true;
                } else {
                    transitiveLbl.setTextFill(Color.RED);
                  return false;
                }
              }
            
              /**
               * Checks whether the graph is anti-symmetric.
               *
               * @return true if the graph is anti-symmetric, false otherwise.
               */
              public  boolean isAntiSymmetric() {
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
                    antiSymmetricLbl.setTextFill(Color.GREEN);
                  return true;
                } else {
                    antiSymmetricLbl.setTextFill(Color.RED);
            
                  return false;
                }
              }
   
       


    @FXML 
    public  boolean isEquivalence(){
        if(isReflexive() && isSymmetric() && isTransitive()){
            equivalenceLbl.setTextFill(Color.GREEN);
            return true;
        }
        equivalenceLbl.setTextFill(Color.RED);
        return false;}

    @FXML
    public boolean isPartialOrder(){
        if(isReflexive() && isAntiSymmetric() && isTransitive()){
            partialOrderLbl.setTextFill(Color.GREEN);
            return true;
        }
        partialOrderLbl.setTextFill(Color.RED);
        return false;
    }
     @FXML
      public  boolean isSimple(){
        
        for (Edge edge : edges) {
         
            if (edge.getDestinationNode().getNodeNum()==(edge.getSource().getNodeNum())) {
                simpleGraphLbl.setTextFill(Color.RED);
              return false;
            }
          }
        

        for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getSource().getNodeNum()==(edge1.getSource().getNodeNum())
                && edge.getDestinationNode().getNodeNum()==(edge1.getDestinationNode().getNodeNum())
                && edge != edge1) {
                    simpleGraphLbl.setTextFill(Color.RED);
              return false;
            }
          }
        }
        simpleGraphLbl.setTextFill(Color.GREEN);
        return true;
      }

      @FXML
      public boolean isMulti(){
        /*for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getSource().getNodeNum()==(edge1.getSource().getNodeNum())
                && edge.getDestinationNode().getNodeNum()==(edge1.getDestinationNode().getNodeNum())
                && edge != edge1) {
                    multiGraphLbl.setTextFill(Color.GREEN);
              return true;
            }
          }
        }*/
        multiGraphLbl.setTextFill(Color.RED);
        return false;
      }

      @FXML
      public boolean isPseudo(){
        for (Edge edge : edges) {
         
           if(edge.getSource().getNodeNum()==(edge.getDestinationNode().getNodeNum())){
                    pseudoGraphLbl.setTextFill(Color.GREEN);
              return true;
            }
          
        }
        pseudoGraphLbl.setTextFill(Color.RED);
        return false;
      }

    @FXML
    public  boolean isComplete(){
        int completeEdgeCounter = 0;
        for (Edge edge : edges) {
          for (Edge edge1 : edges) {
            if (edge.getDestinationNode().getNodeNum()==(edge1.getSource().getNodeNum())) {
              completeEdgeCounter++;
            }
          }
        }
        if (completeEdgeCounter == edges.size() * edges.size()) {
            completeGraphLbl.setTextFill(Color.GREEN);
          return true;
        } 
            completeGraphLbl.setTextFill(Color.RED);
          return false;
        
      }

      @FXML
      public boolean isConnected(){
        Set<Integer> visited = new HashSet<>();

        // Choose any vertex as the starting point
        int startVertex = 0;

        // Perform DFS from the starting vertex
        dfs(edges, startVertex, visited);

        // Check if all vertices are visited
        if(visited.size() == AppState.numNodes)
        {
            connectedLbl.setTextFill(Color.GREEN);
            return true;
        }
        connectedLbl.setTextFill(Color.RED);
        return false;
      }

      public boolean isEulerCircuit(){
        if(isConnected()){
            for(Node node:nodes){
                if(node.getinDegree()==node.getoutDegree()){
                    System.out.println("rff");
                    if((node.getinDegree()+node.getoutDegree())%2==0){
                        System.out.println("rd");
                        eulerCircuitLbl.setTextFill(Color.GREEN);

                        return true;
                    }
                    
                   
                }
            }
           
        }
        eulerCircuitLbl.setTextFill(Color.RED);

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
            } else if (edge.getDestinationNode().getNodeNum() == vertex) {
                dfs(edges, edge.getSource().getNodeNum(), visited);
            }
        }
    }

      
    

}