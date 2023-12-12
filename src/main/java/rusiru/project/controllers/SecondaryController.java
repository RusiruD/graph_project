package rusiru.project.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static List<List<Integer>> adjacencyList = new ArrayList<List<Integer>>();

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
    @FXML  Label acyclicLbl;
    @FXML  Label bipartiteLbl;
    @FXML  Label treeLbl;
    @FXML Label directedSimpleGraphLbl;
    @FXML Label directedMultiGraphLbl;
    @FXML Label directedPseudoGraphLbl;
  
    
     @FXML
    private void initialize(){
        root.setPadding(new Insets(20));
         

        // Generate a random integer between 100 and 300 (inclusive)
       
       
        for(int i=0; i<AppState.numNodes; i++){
            createNode(i);
            adjacencyList.add(new ArrayList<Integer>());
              
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
          
        if(AppState.previousStackPane!=null){
        AppState.previousStackPane.getChildren().get(0).setStyle("-fx-fill: Black;");}
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
        isSimpleDirected();
        isMultiDirected();
        isPseudoDirected();
       
        isComplete();
        isConnected();
        isEulerCircuit();
        isCyclic();
        isBipartite(); 
        isTree();
       
      }


    @FXML
    public boolean isReflexive(){

       int reflexiveEdgeCounter=0;
        for(Edge x:edges){
        
            if(x.getSource().getNodeNum()==x.getDestinationNode().getNodeNum()){
           
                reflexiveEdgeCounter++;
              }
        }

      
      
      if(reflexiveEdgeCounter==AppState.numNodes){
          reflexiveLbl.setTextFill(Color.GREEN);
          return true;
      }
          
               
      reflexiveLbl.setTextFill(Color.RED);
      
      return false;
    }


    @FXML
    public  boolean isSymmetric(){
      if(AppState.undirected){
        symmetricLbl.setTextFill(Color.GREEN);
        return true;}

        int symmetricEdgeCounter = 0;
        for(Edge edge:edges){
            for(Edge edge1:edges){

                if (((edge.getDestinationNode().getNodeNum())==(edge1.getSource().getNodeNum()))
                && ((edge.getSource().getNodeNum())==(edge1.getDestinationNode().getNodeNum()))){
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
                if(AppState.undirected){
                    transitiveLbl.setTextFill(Color.RED);
                    return false;
                }
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
                        && (edge1.getDestinationNode().getNodeNum()!=(edge.getSource().getNodeNum()) && edge1!=edge && edge.getDestinationNode()!=edge.getSource() && edge1.getDestinationNode()!=edge1.getSource())) {
                      possibleTransitiveEdgeCounter++;
                      System.out.println("transitive edge possible " + possibleTransitiveEdgeCounter);
                      System.out.println("source"+ edge.getSource().getNodeNum() + "destination" + edge.getDestinationNode().getNodeNum());
                      System.out.println("source"+ edge1.getSource().getNodeNum() + "destination" + edge1.getDestinationNode().getNodeNum());

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
        if(!AppState.undirected){
                              simpleGraphLbl.setTextFill(Color.RED);

            return false;
        }
       
        
        
        
        else if(containsSelfLoop()){
            simpleGraphLbl.setTextFill(Color.RED);
            return false;
          }
        

        else if(containsMultiEdges()){
        simpleGraphLbl.setTextFill(Color.RED);
        return false;}


        else {
          simpleGraphLbl.setTextFill(Color.GREEN);
        
        return true;}

      }


      @FXML 
      public boolean isSimpleDirected(){
          if(AppState.undirected){
             directedSimpleGraphLbl.setTextFill(Color.RED);
            
              return false;
          }
          
          else if(containsSelfLoop()){
              directedSimpleGraphLbl.setTextFill(Color.RED);
              return false;
            }
          else if(containsMultiEdges()){
              directedSimpleGraphLbl.setTextFill(Color.RED);
              return false;
            }
        

        else{
        directedSimpleGraphLbl.setTextFill(Color.GREEN);
        return true;}

      }

      @FXML
      public boolean isMulti(){
        if(!AppState.undirected){
          multiGraphLbl.setTextFill(Color.RED);
          return false;
        }

      else {
       if(containsMultiEdges() & !containsSelfLoop()){
                    multiGraphLbl.setTextFill(Color.GREEN);
              return true;
            }
        else{
          multiGraphLbl.setTextFill(Color.RED);
          return false;
        }}

          
      }
      @FXML
      public boolean isMultiDirected(){
        if(AppState.undirected){
          return false;
        }

        
        else {
          
       if(containsMultiEdges() && !containsSelfLoop()){
        directedMultiGraphLbl.setTextFill(Color.GREEN);
        return true;}
        else{
          directedMultiGraphLbl.setTextFill(Color.RED);
          return false;
        }}


      }

      @FXML
      public boolean isPseudo(){
       

        if(AppState.undirected){
                  pseudoGraphLbl.setTextFill(Color.RED);

          return false;
        }

        if(containsMultiEdges() && containsSelfLoop()){
          pseudoGraphLbl.setTextFill(Color.GREEN);
          return true;
        }
        else{
          pseudoGraphLbl.setTextFill(Color.RED);
          return false;
        }
      }

    @FXML 
    public boolean isPseudoDirected(){
      
        if(AppState.undirected){
                  directedPseudoGraphLbl.setTextFill(Color.RED);

          return false;
        }
       
        if(containsMultiEdges() && containsSelfLoop()){
          directedPseudoGraphLbl.setTextFill(Color.GREEN);
          return true;
        }
        else{
          directedPseudoGraphLbl.setTextFill(Color.RED);
          return false;
        }
      }

      

    @FXML
    public  boolean isComplete(){
        int completeEdgeCounter = 0;
        for(Node node:nodes){
          if((node.getinDegree()+node.getoutDegree())!=AppState.numNodes-1){
            completeGraphLbl.setTextFill(Color.RED);
            return false;
           
               
                }
                completeGraphLbl.setTextFill(Color.GREEN);
            return true;
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
                    if((node.getinDegree()+node.getoutDegree())%2==0){
                        eulerCircuitLbl.setTextFill(Color.GREEN);

                        return true;
                    }
                    
                   
                }
            }
           
        }
        eulerCircuitLbl.setTextFill(Color.RED);

        return false;
      }

      public boolean isCyclic(){
        boolean[] visited = new boolean[AppState.numNodes];

        for (int i = 0; i < AppState.numNodes; i++) {
            if (!visited[i] && isCyclicDFS(i, -1, visited)) {
                acyclicLbl.setTextFill(Color.RED);
                return true;
            }
        }

        acyclicLbl.setTextFill(Color.GREEN);
        return false;

      }
    
      private boolean isCyclicDFS(int current, int parent, boolean[] visited) {
        visited[current] = true;

        for (int neighbor : adjacencyList.get(current)) {
            if (!visited[neighbor]) {
                // If the neighbor is not visited, continue DFS
                if (isCyclicDFS(neighbor, current, visited)) {
                    return true;
                }
            } else if (neighbor != parent) {
                // If the neighbor is visited and not the parent, there is a cycle
                return true;
            }
        }

        return false;
    }

       
    

      public boolean isBipartite(){
         int[] colors = new int[AppState.numNodes];
        Arrays.fill(colors, -1);  // Initialize colors as -1 (unvisited)

        for (int i = 0; i < AppState.numNodes; i++) {
            if (colors[i] == -1 && !isBipartiteDFS(edges, i, 0, colors)) {
                bipartiteLbl.setTextFill(Color.RED);
                return false;
            }
        }
        bipartiteLbl.setTextFill(Color.GREEN);
        return true;
      }
      private static boolean isBipartiteDFS(List<Edge> edges, int vertex, int color, int[] colors) {
        colors[vertex] = color;

        for (Edge edge : edges) {
            int source = edge.getSource().getNodeNum();
            int destination = edge.getDestinationNode().getNodeNum();

            if (source == vertex) {
                int neighbor = destination;
                if (colors[neighbor] == -1) {
                    // Recursively check the neighbor with the opposite color
                    if (!isBipartiteDFS(edges, neighbor, 1 - color, colors)) {
                        return false;
                    }
                } else if (colors[neighbor] == color) {
                    // If the neighbor has the same color, the graph is not bipartite
                    return false;
                }
                // If the neighbor has a different color, continue to the next neighbor
            }
        }

        
        return true;}


      public boolean isTree(){
        if(isConnected() && !isCyclic()){
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
            } else if (edge.getDestinationNode().getNodeNum() == vertex) {
                dfs(edges, edge.getSource().getNodeNum(), visited);
            }
        }
    }

      
    public boolean containsSelfLoop(){
        for(Edge edge:edges){
            if(edge.getSource().getNodeNum()==edge.getDestinationNode().getNodeNum()){
                return true;
            }
        }
        return false;
    }
    public boolean containsMultiEdges(){
        for(Edge edge:edges){
            for(Edge edge1:edges){
                if(edge.getSource().getNodeNum()==edge1.getSource().getNodeNum() && edge.getDestinationNode().getNodeNum()==edge1.getDestinationNode().getNodeNum() && edge!=edge1){
                    return true;
                }
            }
        }
        return false;
    }

}