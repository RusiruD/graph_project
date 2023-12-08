package rusiru.project;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController {
    @FXML Button submitBtn;
    @FXML TextField numNodeTextField;
    int numNodesInt;

    @FXML
    private void switchToSecondary() throws IOException {

     
        App.setRoot("secondary");
    }

    @FXML
    private void onSubmitClicked() throws IOException, NumberFormatException {
        System.out.println("Submit clicked");
        System.out.println("Number of nodes: " + numNodeTextField.getText());
      
        if(numNodeTextField.getText().equals("")){
            
        }
        else{
            numNodesInt = Integer.parseInt(numNodeTextField.getText());
        AppState.numNodes = numNodesInt;}
       
       switchToSecondary();
    }
}
