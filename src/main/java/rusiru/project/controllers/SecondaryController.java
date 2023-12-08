package rusiru.project.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import rusiru.project.App;
import rusiru.project.AppState;
import rusiru.project.Node;

public class SecondaryController {

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
}