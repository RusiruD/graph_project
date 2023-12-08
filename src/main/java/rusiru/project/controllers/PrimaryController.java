package rusiru.project.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import rusiru.project.App;
import rusiru.project.AppState;

public class PrimaryController {
    @FXML Button submitBtn;
    @FXML TextField numNodesTextField;
    @FXML CheckBox undirected;
    int numNodesInt;

    @FXML
    private void switchToSecondary() throws IOException {

     
        App.setRoot("secondary");
    }

    @FXML
    private void onSubmitClicked() throws IOException, NumberFormatException {
        AppState.undirected = undirected.isSelected();
        System.out.println("Submit clicked");
        System.out.println("Number of nodes: " + numNodesTextField.getText());
      
        if(numNodesTextField.getText().equals("")){
            
        }
        else{
            System.out.println("Number of nodes: " + numNodesTextField.getText());
            numNodesInt = Integer.parseInt(numNodesTextField.getText());
            AppState.numNodes = numNodesInt;}
       
       switchToSecondary();
    }
}
