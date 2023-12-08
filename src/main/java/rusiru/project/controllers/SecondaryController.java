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
}