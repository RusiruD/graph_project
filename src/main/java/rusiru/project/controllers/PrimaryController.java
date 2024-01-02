package rusiru.project.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import rusiru.project.App;
import rusiru.project.AppState;

public class PrimaryController {
  @FXML Button submitBtn;
  @FXML TextField numNodesTextField;
  @FXML CheckBox undirected;
  @FXML CheckBox weighted;
  @FXML Pane introPane;
  @FXML Pane root;
  int numNodesInt;

  @FXML
  private void initialize() {
    introPane
        .layoutXProperty()
        .bind(
            Bindings.divide(Bindings.subtract(root.widthProperty(), introPane.widthProperty()), 2));
  }

  @FXML
  private void switchToSecondary() throws IOException {

    App.setRoot("secondary");
    App.setStage(950, 560);
  }

  @FXML
  private void onSubmitClicked() throws IOException, NumberFormatException {
    AppState.undirected = undirected.isSelected();
    AppState.weighted = weighted.isSelected();
    System.out.println("Submit clicked");
    System.out.println("Number of nodes: " + numNodesTextField.getText());

    if (numNodesTextField.getText().equals("")) {

    } else {
      System.out.println("Number of nodes: " + numNodesTextField.getText());
      numNodesInt = Integer.parseInt(numNodesTextField.getText());
      AppState.numNodes = numNodesInt;
    }

    switchToSecondary();
  }
}
